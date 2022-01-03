package kr.co.infotech.monitor.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class CommonDaoImpl implements CommonDao{

    @Resource(name="sqlSessionTemplate")
    private SqlSession sqlSessionTemplate;

    @Override
    public List<Object> ds01SL(String statement, Object param) throws Exception {
        return sqlSessionTemplate.selectList(statement,param);
    }

    @Override
    public List<String> ds02SL(String statement, String param) throws Exception {
        return sqlSessionTemplate.selectList(statement,param);
    }

    @Override
    public Object ds01SR(String statement, Object param) throws Exception {
        return sqlSessionTemplate.selectOne(statement,param);
    }

    @Override
    public Map<String, String> ds02SR(String statement, String param) throws Exception {
        return sqlSessionTemplate.selectOne(statement,param);
    }

    @Override
    public int ds01IR(String statement, Object param) throws Exception {
        return sqlSessionTemplate.insert(statement,param);
    }
}
