package com.aaa.ljz.base;


import com.aaa.ljz.utils.Map2BeanUtils;
import com.aaa.ljz.utils.SpringContextUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Company AAA软件教育
 * @Author Seven Lee
 * @Date Create in 2020/5/11 15:39
 * @Description
 *      通用service，这个泛型T指的是实体类
 *      也就是说传递什么样的实体类进来，最终所注入的mapper就是什么类型
 **/
public abstract class BaseSerivce<T> {

    private Class<T> cache = null; // 本地缓存池

    @Autowired
    private Mapper<T> mapper;

    /**
     * @author Seven Lee
     * @description
     *      上面的这个Mapper为了保证安全性，必须要是private类型，那么他的子类(UserService)就调用不到
     *      所以需要提供方法给子类用
     * @param []
     * @date 2020/5/11
     * @return tk.mybatis.mapper.common.Mapper
     * @throws
    **/
    protected Mapper getMapper() {
        return mapper;
    }

    /**
     * @author Seven Lee
     * @description
     *      新增功能
     * @param [t]
     * @date 2020/5/11
     * @return java.lang.Integer
     * @throws
    **/
    public Integer add(T t) throws Exception {
        return mapper.insertSelective(t);
    }

    /**
     * @author Seven Lee
     * @description
     *      通过主键删除
     * @param [t]
     * @date 2020/5/11
     * @return java.lang.Integer
     * @throws
    **/
    public Integer delete(T t) throws Exception {
        return mapper.deleteByPrimaryKey(t);
    }

    /**
     * @author Seven Lee
     * @description
     *      通过主键批量删除
     *      能用java代码来搞定的东西，千万不要上子查询
     *      阿里巴巴代码规约手册:
     *          如果联查的时候超过两张表，那么你一定要把这个联查拆开，放在java代码中实现
     * @param [ids]
     * @date 2020/5/11
     * @return java.lang.Integer
     * @throws
    **/
    public Integer batchDelete(List<Object> ids) throws Exception {
        Example example = Example.builder(getTypeArguement()).where(Sqls.custom().andIn("id", ids)).build();
        return mapper.deleteByExample(example);
    }

    /**
     * @author Seven Lee
     * @description
     *      更新功能
     * @param [t]
     * @date 2020/5/11
     * @return java.lang.Integer
     * @throws
    **/
    public Integer update(T t) throws Exception {
        return mapper.updateByPrimaryKeySelective(t);
    }

    /**
     * @author Seven Lee
     * @description
     *      批量更新
     *      t:所要更新的数据(t代表实体类，只能存放一个id)
     *      ids:通用主键来进行更新
     * @param [t, ids, properties]
     * @date 2020/5/11
     * @return java.lang.Integer
     * @throws
    **/
    public Integer batchUpdate(T t, Object[] ids) throws Exception {
        Example example = Example.builder(getTypeArguement()).where(Sqls.custom().andIn("id", Arrays.asList(ids))).build();
        return mapper.updateByExample(t, example);
    }

    /**
     * @author Seven Lee
     * @description
     *      查询一条数据
     * @param [t]
     * @date 2020/5/11
     * @return T
     * @throws
    **/
    public T queryOne(T t) throws Exception {
        return mapper.selectOne(t);
    }

    /**
     * @author Seven Lee
     * @description
     *      通过指定字段查询一条数据
     *      (有些表中有唯一键(username, iphone_num...))
     * @param [where, orderByField, fields]
     * @date 2020/5/11
     * @return T
     * @throws
    **/
    public T queryByField(Sqls where, String orderByField, String... fields) throws Exception {
        return (T) queryByFieldsBase(null, null, where, orderByField, fields).get(0);
    }

    /**
     * @author Seven Lee
     * @description
     *      条件查询集合
     * @param [where, orderByField, fields]
     * @date 2020/5/11
     * @return java.util.List<T>
     * @throws
    **/
    public List<T> queryListByFields(Sqls where, String orderByField, String... fields) throws Exception {
        return queryByFieldsBase(null, null, where, orderByField, fields);
    }

    /**
     * @author Seven Lee
     * @description
     *      条件查询分页
     * @param [pageNo, pageSize, where, orderByFileds, fields]
     * @date 2020/5/11
     * @return com.github.pagehelper.PageInfo<T>
     * @throws 
    **/
    public PageInfo<T> queryListByPageAndFields(Integer pageNo, Integer pageSize, Sqls where, String orderByFileds, String... fields) throws Exception {
        return new PageInfo<T>(queryByFieldsBase(pageNo, pageSize, where, orderByFileds, fields));
    }

    /**
     * @author Seven Lee
     * @description
     *      条件查询
     * @param [t]
     * @date 2020/5/11
     * @return java.util.List<T>
     * @throws
    **/
    public List<T> queryList(T t) throws Exception {
        return mapper.select(t);
    }

    /**
     * @author Seven Lee
     * @description
     *      分页查询
     * @param [t, pageNo, pageSize]
     * @date 2020/5/11
     * @return com.github.pagehelper.PageInfo<T>
     * @throws
    **/
    public PageInfo<T> queryListByPage(T t, Integer pageNo, Integer pageSize) throws Exception {
        PageHelper.startPage(pageNo, pageSize);
        List<T> select = mapper.select(t);
        PageInfo<T> pageInfo = new PageInfo<T>(select);
        return pageInfo;
    }

    /**
     * @author Seven Lee
     * @description
     *      根据反射获取实例对象
     * @param [map]
     * @date 2020/5/12
     * @return T
     * @throws
    **/
    public T newInstance(Map map) {
        return (T) Map2BeanUtils.map2Bean(map, getTypeArguement());
    }

    /**
     * @author Seven Lee
     * @description
     *      封装条件查询，分页查询以及排序查询的通用方法(多条件查询)
     * @param [pageNo, pageSize, where, orderByField, field]
     * @date 2020/5/11
     * @return java.util.List<T>
     * @throws
    **/
    private List<T> queryByFieldsBase(Integer pageNo, Integer pageSize, Sqls where, String orderByField, String... field) {
        Example.Builder builder = null;
        if(null == field || field.length == 0) {
            // 没有条件查询，说明查询的是所有数据
            builder = Example.builder(getTypeArguement());
        } else {
            // 指定某些/某个字段进行查询
            builder = Example.builder(getTypeArguement())
                    .select(field);
        }
        if(null != where) {
            builder = builder.where(where);
        }

        if(null != orderByField) {
            builder = builder.orderByDesc(orderByField);
        }

        Example example = builder.build();

        if(null != pageNo && null != pageSize) { // pageHelper通用分页插件
            PageHelper.startPage(pageNo, pageSize);
        }

        List list = getMapper().selectByExample(example);
        return list;
    }

    /**
     * @author Seven Lee
     * @description
     *      获取子类泛型类型
     * @param []
     * @date 2020/5/11
     * @return java.lang.Class<T>
     * @throws
    **/
    private Class<T> getTypeArguement() {
        if(null == cache) {
            cache = (Class<T>) ((ParameterizedType)(this.getClass().getGenericSuperclass())).getActualTypeArguments()[0];
        }
        return cache;
    }

    /**
     * @author Seven Lee
     * @description
     *      获取String容器
     *      TODO 咱们用不到，有待补充
     * @param []
     * @date 2020/5/12
     * @return org.springframework.context.ApplicationContext
     * @throws
    **/
    public ApplicationContext getApplicationContext() {
        return SpringContextUtils.getApplicationContext();
    }

}
