/**
 * 
 * APDPlat - Application Product Development Platform
 * Copyright (c) 2013, 杨尚川
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package com.apdplat.module.monitor.action;

import com.apdplat.module.monitor.model.RuningTime;
import com.apdplat.module.monitor.service.RuningTimeChartDataService;
import com.apdplat.module.monitor.service.RuningTimeSingleService;
import com.apdplat.module.system.service.LogQueue;
import com.apdplat.platform.action.ExtJSSimpleAction;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Scope("prototype")
@Controller
@Namespace("/monitor")
public class RuningTimeAction extends ExtJSSimpleAction<RuningTime> {
    private String category;
    @Resource(name="runingTimeSingleService")
    private RuningTimeSingleService runingTimeSingleService;
    @Override
    public String query(){
        LogQueue.getLogQueue().saveLog();
        return super.query();
    }
    @Override
    protected void afterRender(Map map,RuningTime obj){
        map.put("runingTime", obj.getRuningTimeStr());
        map.remove("osName");
        map.remove("osVersion");
        map.remove("osArch");
        map.remove("jvmVersion");
        map.remove("jvmName");
        map.remove("jvmVendor");
        map.remove("updateTime");
        map.remove("createTime");
        map.remove("appName");
    }
    @Override
    protected String generateReportData(List<RuningTime> models) {
        LinkedHashMap<String,Long> data=new LinkedHashMap<>();
        if("runingRate".equals(category)){
            data=RuningTimeChartDataService.getRuningRateData(models);
        }
        if("runingSequence".equals(category)){
            data=RuningTimeChartDataService.getRuningSequence(models);
        }
        
        return runingTimeSingleService.getXML(data);
    }

    public void setCategory(String category) {
        this.category = category;
    }
}