package net.dlyt.qyds.web.service.impl;

import net.dlyt.qyds.common.dto.MmbMaster;
import net.dlyt.qyds.dao.MmbMasterMapper;
import net.dlyt.qyds.dao.ext.MmbMasterMapperExt;
import net.dlyt.qyds.web.service.AddPointCumulativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("addToPoint")
public class AddPointCumulativeServiceImpl implements AddPointCumulativeService {
    @Autowired
    MmbMasterMapper mmbMasterMapper;

    @Override
    public void addToPoint() {
        //将所有会员的积分放入到累计消费中
        List<MmbMaster> mmbMasters = mmbMasterMapper.selectAllMaster();
        System.out.println("-----------------------START-------------------------");
        for (MmbMaster mmbMaster:mmbMasters) {
            if (mmbMaster!=null){
                mmbMaster.setPointCumulative(mmbMaster.getAllPoint());
                mmbMasterMapper.updateByPrimaryKeySelective(mmbMaster);
                System.out.println("累计积分："+mmbMaster.getPointCumulative());
            }
        }
        System.out.println("-------------------------END--------------------------");
    }
}
