package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.BnkRecords;
import net.dlyt.qyds.common.dto.ext.BnkRecordsExt;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Created by wy on 2016/8/3.
 */
@Repository
public interface BnkRecordsMapperExt {
     /*获取商品履历列表*/
    List<BnkRecordsExt> selectAll(BnkRecordsExt form);

    /*获取商品履历总行数*/
    int getAllDataCount(BnkRecords records);
}
