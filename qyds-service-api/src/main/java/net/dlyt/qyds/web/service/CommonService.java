package net.dlyt.qyds.web.service;

import net.dlyt.qyds.common.dto.ComCode;
import net.dlyt.qyds.common.dto.SysCode;

import java.util.List;

/**
 * Created by congkeyan on 2016/7/14.
 */
public interface CommonService {

    //根据参数获取码表数据
    List<ComCode> selectComCodeByCategory(String category);
}
