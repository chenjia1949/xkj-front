package com.cwlrdc.front.para.ctrl;

import com.cwlrdc.commondb.para.entity.ParaSysparame;
import com.cwlrdc.commondb.para.entity.ParaSysparameKey;
import com.cwlrdc.front.common.ParaSysparameCache;
import com.cwlrdc.front.para.service.ParaSysparameService;
import com.joyveb.lbos.restful.common.DbCondi;
import com.joyveb.lbos.restful.common.ListInfo;
import com.joyveb.lbos.restful.common.PageInfo;
import com.joyveb.lbos.restful.common.ReturnInfo;
import com.joyveb.lbos.restful.spring.FieldsMapperBean;
import com.joyveb.lbos.restful.spring.QueryMapperBean;
import com.joyveb.lbos.restful.spring.RequestJsonParam;
import com.joyveb.lbos.restful.util.KeyExplainHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/paraSysparame")
public class ParaSysparameCtrl {
	
	private @Resource ParaSysparameService dbService;
	private @Resource ParaSysparameCache pscService;
	
	@RequestMapping(value="",method=RequestMethod.POST)
	@ResponseBody
	public ReturnInfo insert(@RequestBody ParaSysparame info,HttpServletRequest req) {
		try {
			ParaSysparame paraSysparame = dbService.selectByPrimaryKey(info);
			if(null!=paraSysparame){
				return new ReturnInfo("记录已经存在",false);
			}
			dbService.insert(info);
			pscService.reload();
			return ReturnInfo.Success;
		}catch (DuplicateKeyException e){
			log.trace("主键冲突",e);
			return new ReturnInfo("添加失败,不可重复添加",false);
		} catch (Exception e) {
			log.warn("  paraSysparame insert error..", e);
		}
		return ReturnInfo.Faild;
	}
	
	@RequestMapping(value="",method=RequestMethod.PUT)
	@ResponseBody
	public ReturnInfo update(@RequestBody ParaSysparame info,HttpServletRequest req) {
		try {
			dbService.updateByExample(info,dbService.getExample(info));
			pscService.reload();
			return ReturnInfo.Success;
		} catch (Exception e) {
			log.warn("  ParaSysparameCtrl update error..",e);
			
		}
		return ReturnInfo.Faild;
	}
	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public Object get(@RequestJsonParam(value = "query",required=false) QueryMapperBean info,
			@RequestJsonParam(value = "fields",required=false) FieldsMapperBean fmb,
			PageInfo para, HttpServletRequest req) {
		int totalCount = 0;
		List<HashMap<String,Object>> list = null;
		try {
			DbCondi dc = new DbCondi();
			dc.setEntityClass(ParaSysparame.class);
			dc.setKeyClass(ParaSysparameKey.class);
			dc.setQmb(info);
			dc.setPageinfo(para);
			dc.setFmb(fmb);
			dc.setTalbeName(getTableName());
			totalCount = dbService.getCount(dc);
			list = dbService.getData(dc);
		} catch (Exception e) {
			log.warn("  ParaSysparameCtrl get error..",e);
			
		}
		if(para.isPage()){
			return new ListInfo<>(totalCount, list, para);
		}else{
			return list;
		}
	}
	
	@RequestMapping(value="/batch/delete",method=RequestMethod.POST)
	@ResponseBody
	public ReturnInfo batchDelete(@RequestBody List<String> data,HttpServletRequest req) {
		try {
			List<ParaSysparame> list = new ArrayList<ParaSysparame>();
			for(String id :data){
				ParaSysparame info = new ParaSysparame();
				KeyExplainHandler.explainKey(id, info);
				list.add(info);
			}
			dbService.batchDelete( list);
			pscService.reload();
			return ReturnInfo.Success;
		} catch (Exception e) {
			log.warn("  ParaSysparameCtrl batchDelete error..",e);
			
		}
		return ReturnInfo.Faild;
	}
	
	@RequestMapping(value="/batch",method=RequestMethod.PUT)
	@ResponseBody
	public ReturnInfo batchUpdate(@RequestBody ParaSysparames data,HttpServletRequest req) {
		try {
			dbService.batchUpdate(data);
			pscService.reload();
			return ReturnInfo.Success;
		} catch (Exception e) {
			log.warn("  ParaSysparameCtrl batchUpdate error..",e);
			
		}
		return ReturnInfo.Faild;
	}
	
	@RequestMapping(value="/batch",method=RequestMethod.POST)
	@ResponseBody
	public ReturnInfo batchInsert(@RequestBody ParaSysparames data,HttpServletRequest req) {
		try {
			dbService.batchInsert( data);
			pscService.reload();
			return ReturnInfo.Success;
		} catch (Exception e) {
		    log.warn("  ParaSysparameCtrl batchInsert error..",e);
			
		}
		return ReturnInfo.Faild;
	}
	
	@RequestMapping(value="/{key}",method=RequestMethod.GET)
	@ResponseBody
	public ListInfo<ParaSysparame> get(@PathVariable String key,HttpServletRequest req) {
		int totalCount = 1;
		List<ParaSysparame> list = new ArrayList<>();
		try {
			ParaSysparame info = new ParaSysparame();
			KeyExplainHandler.explainKey(key, info);
			list.add(dbService.selectByPrimaryKey(info));
		} catch (Exception e) {
			log.warn("  ParaSysparameCtrl get by key error..",e);
		}
		return  new ListInfo<>(totalCount, list, 0, 1);
	}
	
	@RequestMapping(value="/{key}",method=RequestMethod.DELETE)
	@ResponseBody
	public ReturnInfo delete(@PathVariable String key,HttpServletRequest req) {
		try {
			ParaSysparame info = new ParaSysparame();
			KeyExplainHandler.explainKey(key, info);
			dbService.deleteByPrimaryKey(info);
			pscService.reload();
			return ReturnInfo.Success;
		} catch (Exception e) {
			log.warn("  ParaSysparameCtrl delete by key error..",e);
		}
		return ReturnInfo.Faild;
	}
	
	@RequestMapping(value="/{key}",method=RequestMethod.PUT)
	@ResponseBody
	public ReturnInfo update(@PathVariable String key,@RequestBody ParaSysparame info,HttpServletRequest req) {
		try {
			ParaSysparame oldPojo = null;
			if(info!=null){
				KeyExplainHandler.explainKey(key, info);
				oldPojo = dbService.selectByPrimaryKey(info);
				dbService.updateByPrimaryKey(info);
				pscService.reload();
			}
			return ReturnInfo.Success;
		} catch (Exception e) {
			log.warn("  ParaSysparameCtrl update by key error..",e);
		}
		return ReturnInfo.Faild;
	}
	
	private String getControllerName(){
		return this.getClass().getSimpleName();
	}
	
	private String getTableName(){
		return "T_PARA_SYSPARAME";
	}
	
	@SuppressWarnings("serial")
	public static class ParaSysparames extends ArrayList<ParaSysparame> {  
	    public ParaSysparames() { super(); }  
	}
	
}
