import argparse, socket, time, json, platform, psutil, requests, pprint, uuid, math, pymysql
import configparser

#initial values
parser = argparse.ArgumentParser(description='Monitoring script to send system info to a tracking server')
parser.add_argument('-d', '--dest', default='http://localhost:8080/', help='API Endpoint for Monitoring Data (Defaults to http://localhost:8080/)')
parser.add_argument('-i', '--interval', default=60, type=int, help='Interval between checks (Seconds. Defaults to 60 seconds)')
parser.add_argument('-a', '--attempts', default=10, type=int, help='Attempts to send data when sending failes (Defaults to 10)')
parser.add_argument('-t', '--timeout', default=60, type=int, help='Timeout between resend attempts (Seconds. Defaults to 60. If attempts is reached script will die)')
args = parser.parse_args()

#external configuration
config = configparser.ConfigParser()
config.read('config.ini')

def getServerInfo():
    #system_uuid = uuid.getnode()
    global config 
    
    system_uuid = str(uuid.uuid1().hex)
    
    disks = {}
    for x in psutil.disk_partitions():
        try:
            disks[x.device] = {
                "disk_name" : x.device,
                "disk_size" : convert_size(psutil.disk_usage(x.mountpoint).total),
                "disk_percent" : str(psutil.disk_usage(x.mountpoint).percent)
            }
            # disks.append({
            #     "disk_name" : x.device,
            #     "disk_size" : convert_size(psutil.disk_usage(x.mountpoint).total),
            #     "disk_percent" : str(psutil.disk_usage(x.mountpoint).percent)
            # })
        except:
            continue

    server_info = {
        "publicip" : config['SERVER']['PUBLICIP'],
        "hostname" : socket.gethostname(),
        "system_uuid" : system_uuid,
        "system_OS" : platform.system() + ' ' + platform.release(),
        "cpu_count" : str(psutil.cpu_count()) + ' Core',
        "cpu_percent" : str(psutil.cpu_percent(interval=1)),
        "memory_size" : convert_size(psutil.virtual_memory().total),
        "memory_percent" : str(psutil.virtual_memory().percent),
        "drives" : disks,
    }
    return server_info
    
def main():
    while True:    
        server_info = getServerInfo()
        print(server_info)
        send_data(json.dumps(server_info))
        print("-----------------------------------------------------------------")
        time.sleep(args.interval)
    

def send_data(data):
    global config
    for attempt in range(args.attempts):
        try:
            # endpoint = args.dest
            headers = {'Content-Type': 'application/json; charset=utf-8'}
            endpoint = config['SERVER']['DESTINATION']
            response = requests.post(url = endpoint, headers = headers, data = data)
            if 200 != response.status_code:
                print('response.status_code :', response.status_code)
                print("API server status_code Exception sleep :", args.timeout)
                time.sleep(args.timeout)
                continue
            else:
                print('response.status_code :', response.status_code)
                break
        except requests.exceptions.RequestException as e:
            print(e)
            print("API server requests Exception sleep :", args.timeout)
            time.sleep(args.timeout)
    else:
        print("system exit(0)")
        exit(0)

def convert_size(size_bytes):
   if size_bytes == 0:
       return "0B"
   size_name = ("B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")
   i = int(math.floor(math.log(size_bytes, 1024)))
   p = math.pow(1024, i)
   s = round(size_bytes / p, 1)
   return "%s %s" % (s, size_name[i])


if __name__ == "__main__":
    main()