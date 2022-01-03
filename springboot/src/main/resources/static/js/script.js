window.onload = function(){
    let table = $('#serverList-table').DataTable();
    table.destroy();

    let ajaxRequest = new XMLHttpRequest();
    ajaxRequest.onreadystatechange = function(){
        if(ajaxRequest.readyState == ajaxRequest.DONE){
            if(ajaxRequest.status == 200 || ajaxRequest.status == 201){
                const data = getServerListData(JSON.parse(ajaxRequest.response));
                console.log(data);
                $('#serverList-table').dataTable({
                    data:data,
                    columns:[
                        {
                            data:"serverNo",
                            render: function(data){
                                return '<input type="checkbox" class="mt-1" value="'+data+'"/>';
                            }
                        },
                        // {
                        //     data:"serverStatus",
                        //     render: function(data){
                        //         return '<p class="mt-1">'+data+'</p>';
                        //     }},
                        {
                            data:"serverName",
                            render: function(data){
                                return '<p class="mt-1">'+data+'</p>';
                            }},
                        {
                            data:"serverCompany",
                            render: function(data){
                                return '<p class="mt-1">'+data+'</p>';
                            }},
                        {
                            data:"serverPublicIp",
                            render: function(data){
                                return '<p class="mt-1">'+data+'</p>';
                            }},
                        {
                            data:"serverPrivateIp",
                            render: function(data){
                                return '<p class="mt-1">'+data+'</p>';
                            }},
                        {
                            data:"serverCpuPercentage",
                            render: function(data){
                                let result = '<div className="text-center" id="example-caption-1"><small>'+data+'%</small></div>';
                                result += '<div class="progress"><div class="progress-bar" role="progressbar" aria-valuenow="'+data+'" aria-valuemin="0" aria-valuemax="100" style="width: '+data+'%"></div></div>';
                                return result;
                            }},
                        {
                            data:"serverMemoryPercentage",
                            render: function(data){
                                let result = '<div className="text-center" id="example-caption-1"><small>'+data+'%</small></div>';
                                result += '<div class="progress"><div class="progress-bar" role="progressbar" aria-valuenow="'+data+'" aria-valuemin="0" aria-valuemax="100" style="width: '+data+'%"></div></div>';
                                return result;
                            }},
                        {
                            data:"serverDiskMemoryPercentage",
                            render: function(data){
                                let result = "";
                                for(let i=0;i<data.length;i++){
                                    result += '<div className="text-center" id="example-caption-1"><small>'+data[i]['serverDiskName']+' / '+data[i]['serverDiskStatusPercentage']+'%</small></div>';
                                    result += '<div class="progress" style="margin-bottom: 0 !important;"><div class="progress-bar" role="progressbar" aria-valuenow="'+data[i]['serverDiskStatusPercentage']+'" aria-valuemin="0" aria-valuemax="100" style="width: '+data[i]['serverDiskStatusPercentage']+'%"></div></div>';
                                }
                                return result;
                            }},
                        {
                            data:"serverDate",
                            render: function(data){
                                return '<p class="mt-1">'+data+'</p>';
                            }},
                        {
                            data:"serverEtc",
                            render: function(data){
                                return '<p class="mt-1">'+data+'</p>';
                            }}
                    ],
                    language:{
                        emptyTable:'검색결과가 없습니다.',
                        search:'검색 : '
                    }
                })
            }else{
                alert('데이터를 불러오는 데 실패했습니다.');
                return;
            }
        }
    }

    ajaxRequest.open('GET','/server/info',true);
    ajaxRequest.setRequestHeader('Content-Type','application/json; charset=utf-8');
    ajaxRequest.send();
}

function getServerListData(outJson){
    console.log(outJson);
    let dataList = [];
    for(let i=0;i<outJson.length;i++){
        let data = {};
        data['serverNo'] = outJson[i]['server']['serverNo'];
        data['serverStatus'] = '상태값'
        data['serverName'] = outJson[i]['server']['serverName'];
        data['serverCompany'] = outJson[i]['server']['serverCompany'];
        data['serverPublicIp'] = outJson[i]['server']['serverPublicIp'];
        data['serverPrivateIp'] = outJson[i]['server']['serverPrivateIp'];
        data['serverCpuPercentage'] = outJson[i]['serverStatus'] ? outJson[i]['serverStatus']['serverStatusCpuPercentage'] : '';
        data['serverMemoryPercentage'] = outJson[i]['serverStatus'] ? outJson[i]['serverStatus']['serverStatusMemoryPercentage'] : '';
        data['serverDiskMemoryPercentage'] = outJson[i]['serverStatus'] ? outJson[i]['serverStatus']['serverDiskStatusList'] : '';
        data['serverEtc'] = outJson[i]['server']['serverEtc'];
        data['serverDate'] = outJson[i]['serverStatus'] ? outJson[i]['serverStatus']['serverStatusTime'] : '';
        dataList.push(data);
    }

    return dataList;
}

document.querySelector('#submitBtn').addEventListener('click',function(){
    const serverInfo = {
        serverName : document.getElementById('serverName').value,
        serverCompany : document.getElementById('serverCompany').value,
        serverPublicIp : document.getElementById('serverPublicIp').value,
        serverPrivateIp : document.getElementById('serverPrivateIp').value,
        serverGubun : document.getElementById('serverGubun').value,
        serverOs : document.getElementById('serverOs').options[document.getElementById('serverOs').selectedIndex].value,
        serverCpu : document.getElementById('serverCpu').options[document.getElementById('serverCpu').selectedIndex].value,
        serverMemory : document.getElementById('serverMemory').options[document.getElementById('serverMemory').selectedIndex].value,
        serverCost : document.getElementById('serverCost').value,
        serverEtc : document.getElementById('descTextarea').value,
        linuxUserName: document.getElementById('linuxUserName').value,
        linuxUserPwd: document.getElementById('linuxUserPwd').value
    }

    let serverDiskInfos = [];
    const serverDiskNames = document.getElementsByName('serverDiskName');
    const serverDiskCapacities = document.getElementsByName('serverDiskCapacity');
    for(let i=0;i<serverDiskNames.length;i++){
        let obj = {
            serverDiskName : serverDiskNames[i].value,
            serverDiskCapacity : serverDiskCapacities[i].value
        }
        serverDiskInfos.push(obj);
    }
    serverInfo['serverDisk'] = serverDiskInfos;
    console.log(serverInfo);

    let ajaxRequest = new XMLHttpRequest();
    ajaxRequest.onreadystatechange = function(){
        if(ajaxRequest.readyState == ajaxRequest.DONE){
            if(ajaxRequest.status == 200 || ajaxRequest.status == 201){
                alert('서버정보가 정상적으로 등록되었습니다.');
                location.reload();
            }else{
                alert('http request not 200');
                return;
            }
        }
    }

    ajaxRequest.open('POST','/server/info',true);
    ajaxRequest.setRequestHeader('Content-Type','application/json; charset=utf-8');
    ajaxRequest.send(JSON.stringify(serverInfo));

});

document.querySelector('#serverOs').addEventListener('change',function(){
    const value = document.getElementById('serverOs').options[document.getElementById('serverOs').selectedIndex].value;
    if(value.indexOf('centOS')>=0){
        $('#linuxModal').modal('show');
    }
});

document.querySelector('#submitBtn2').addEventListener('click',function(){
   const linuxUserName = document.querySelector('#linuxUserNameInput').value;
   const linuxUserPwd = document.querySelector('#linuxUserPwdInput').value;
   if(linuxUserName != null && linuxUserPwd != null && linuxUserName.length>=1 && linuxUserPwd.length>=1){
        document.querySelector('#linuxUserName').value = linuxUserName;
        document.querySelector('#linuxUserPwd').value = linuxUserPwd;
        $('#linuxModal').modal('hide');
   }else{
       alert('서버 정보를 입력하세요');
       return;
   }
});

function addInput(node){
    let form = node.parentNode.children[2];
    let clone = form.cloneNode(true);
    console.log(clone);
    node.parentNode.appendChild(form.cloneNode(true));
}

function removeInput(node){
    const diskArea = document.querySelector('#diskArea');
    if(diskArea.children.length<=3){
        return;
    }
    diskArea.removeChild(node.parentNode.parentNode.parentNode);
}
