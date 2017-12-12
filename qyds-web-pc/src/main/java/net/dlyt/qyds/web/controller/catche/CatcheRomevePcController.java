package net.dlyt.qyds.web.controller.catche;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.service.CatcheRemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by congkeyan on 2016/8/29.
 */
@Controller
@RequestMapping("/catcheRemove")
public class CatcheRomevePcController {

    @Autowired
    private CatcheRemoveService catcheRemoveService;

    /**
     * 清除PC全部缓存
     * @return
     */
    @RequestMapping("removeAll")
    public @ResponseBody
    JSONObject removeAll(){
        return catcheRemoveService.removeAll();
    }

    /**
     * 清除PC部分缓存
     * @return
     */
    @RequestMapping("removeSome")
    public @ResponseBody
    JSONObject removeSome(){
        return catcheRemoveService.removeSome();
    }


    /**
     * 清除PC单个缓存
     * @return
     */
    @RequestMapping("removeOnly")
    public @ResponseBody
    JSONObject removeOnly(){
        return catcheRemoveService.removeOnly();
    }


}
