package net.dlyt.qyds.web.controller.testPointall;

import net.dlyt.qyds.web.service.AddPointCumulativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/testPointAll")
public class TestPointAll {
    @Autowired
    private AddPointCumulativeService addPointCumulativeService;

    @RequestMapping("test")
    public void distributeBirthdayCoupon() {
//        addPointCumulativeService.addToPoint();
        addPointCumulativeService.addAllPoint();

    }
}
