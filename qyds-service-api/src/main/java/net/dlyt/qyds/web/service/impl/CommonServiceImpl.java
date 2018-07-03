package net.dlyt.qyds.web.service.impl;

import net.dlyt.qyds.common.dto.ComCode;
import net.dlyt.qyds.dao.ext.ComCodeMapperExt;
import net.dlyt.qyds.web.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by congkeyan on 2016/7/19.
 */
@Service("commonService")
public class CommonServiceImpl implements CommonService{

    @Autowired
    private ComCodeMapperExt comCodeMapperExt;

    public List<ComCode> selectComCodeByCategory(String category){
        return comCodeMapperExt.selectComCodeByCategory(category);
    }


}
