package com.tongtong.ttmall.common;

import android.content.Context;
import android.text.TextUtils;

import com.tongtong.ttmall.mall.shopping.bean.AddressBean;
import com.tongtong.ttmall.mall.shopping.bean.CityBean;
import com.tongtong.ttmall.mall.shopping.bean.CountyBean;
import com.tongtong.ttmall.mall.shopping.bean.ProvinceBean;
import com.tongtong.ttmall.mall.user.bean.CommonBean;
import com.tongtong.ttmall.retrofit.RetrofitUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lkz on 2016/7/16.
 * 省市县请求工具
 */
public class AreaUtil {
    //省市县Bean
    private AddressBean addressBean;
    //缓存
    private static ACache aCache;

    private AreaUtil() {
    }

    public static AreaUtil getInstance(Context mContext){
        aCache = ACache.get(mContext);
        return new AreaUtil();
    }

    /**
     * 得到省市县bean
     */
    public AddressBean getAddressBean(){
        addressBean = (AddressBean) aCache.getAsObject("addressBean");
        if (addressBean == null){
            RetrofitUtils.connToServer().getAddressList()
                    .enqueue(new Callback<CommonBean<AddressBean>>() {
                        @Override
                        public void onResponse(Call<CommonBean<AddressBean>> call, Response<CommonBean<AddressBean>> response) {
                            addressBean = response.body().getData();
                            aCache.put("addressBean",addressBean);
                        }

                        @Override
                        public void onFailure(Call<CommonBean<AddressBean>> call, Throwable t) {

                        }
                    });
        }
        return addressBean;
    }

    /**
     * 根据省份ID得到省份名字
     * @param proID
     * @return
     */
    public String getProvinceName(String proID){
        List<ProvinceBean> provinceBeanList = getAddressBean().getProvs();
        for (ProvinceBean provinceBean:
             provinceBeanList) {
            if (TextUtils.equals(proID,provinceBean.getId())){
                return provinceBean.getName();
            }
        }
        return "";
    }

    /**
     * 根据城市ID得到城市名字
     * @param cityID
     * @return
     */
    public String getCityName(String cityID){
        List<CityBean> cityBeanList = getAddressBean().getCitys();
        for (CityBean cityBean:
                cityBeanList) {
            if (TextUtils.equals(cityID,cityBean.getId())){
                return cityBean.getName();
            }
        }
        return "";
    }

    /**
     * 根据县ID得到县名字
     * @param countyID
     * @return
     */
    public String getCountyName(String countyID){
        List<CountyBean> countyBeanList = getAddressBean().getCountys();
        for (CountyBean countyBean:
                countyBeanList) {
            if (TextUtils.equals(countyID,countyBean.getId())){
                return countyBean.getName();
            }
        }
        return "";
    }
}
