package kr.co.infotech.monitor.dao;

import java.util.List;
import java.util.Map;

public interface CommonDao {

    List<Object> ds01SL(String statement, Object param) throws Exception;
    List<String> ds02SL(String statement, String param) throws Exception;

    Object ds01SR(String statement, Object param) throws Exception;
    Map<String,String> ds02SR(String statement, String param) throws Exception;

    int ds01IR(String statement, Object param) throws Exception;


}
