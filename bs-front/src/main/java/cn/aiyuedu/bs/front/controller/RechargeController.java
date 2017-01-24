package cn.aiyuedu.bs.front.controller;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.dto.ClientInfo;
import cn.aiyuedu.bs.common.model.BookBase;
import cn.aiyuedu.bs.dao.entity.Chapter;
import cn.aiyuedu.bs.service.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import cn.aiyuedu.bs.common.orm.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Properties;

/**
 * Created by Thinkpad on 2014/12/5.
 */
@Controller
@RequestMapping("/**/ft/recharge/*")
public class RechargeController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PaySwitchGeneralService paySwitchGeneralService;
    @Autowired
    private UserService userService;
    @Autowired
    private SubConfigGeneralService subConfigGeneralService;
    @Autowired
    private BookGeneralService bookGeneralService;
    @Autowired
    private ChapterGeneralService chapterGeneralService;
    @Autowired
    private Properties frontConfig;
    @Autowired
    private RechargeService rechargeService;
    @Autowired
    private RechargeRepository rechargeRepository;

    @RequestMapping("rechargeType.do")
    public ModelAndView rechargeType(HttpServletRequest request,
                               HttpServletResponse response,
                               ModelMap model) {

        //用户ID,平台ID,版本,渠道号
        Integer uid = -1;
        Integer platformId = -1;
        String version = "";
        String ditchId = "";

        ClientInfo cli = (ClientInfo) request.getAttribute("xclient");
        if(null!=cli){
            uid = cli.getUid();
            if(null!=cli.clientParam()){
                platformId = cli.clientParam().getPlatform();
                version = cli.clientParam().getVersion();
                ditchId = null==cli.clientParam().getChannelId()?"":cli.clientParam().getChannelId().toString();
            }
        }

        if(log.isInfoEnabled()){
            log.info("RechargeType it's parameter: uid="+uid
                    +", paltformId="+platformId+", version="+version+", ditchId="+ditchId);
        }

        if(null!=uid){
            User user = userService.get(uid);
            model.put("user", user);
        }else{
            if(log.isInfoEnabled()){
                log.info("rechargeType(): The user is null!!!");
            }
        }

        List<PayBaseDto> rechargeTypeVoList = null;
        PayBaseRedisDto rechargeTypeDto = paySwitchGeneralService.getPaySwitch(platformId, version, ditchId);
        if(null!=rechargeTypeDto){
            //不存在支付方式
            if(null == rechargeTypeDto.getList()){
                rechargeTypeVoList = Lists.newArrayList();

                if(log.isInfoEnabled()){
                    log.info("Non Recharge Type.......");
                }
            }else{
                rechargeTypeVoList = rechargeTypeDto.getList();
            }
        }else{
            if(log.isInfoEnabled()){
                log.info("Non Recharge Type, rechargeTypeDto is null .......");
            }
        }

        model.put("rechargeTypeVoList", rechargeTypeVoList);

        return new ModelAndView("/pages/recharge-type");
    }

    /**
     * 从支付方式跳转到支付配置的Controller
     * @param request
     * @param response
     * @param parentRechargeTypeId (一级)支付方式ID
     * @param model
     * @return
     */
    @RequestMapping("rechargeMoney.do")
    public ModelAndView rechargeMoney(HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestParam(value = "parentRechargeTypeId", required = true) Integer parentRechargeTypeId,
                               ModelMap model) {

        //平台ID,版本,渠道号
        Integer platformId = -1;
        String version = "";
        String ditchId = "";

        ClientInfo cli = (ClientInfo) request.getAttribute("xclient");
        if(null!=cli && null!=cli.clientParam()){
            platformId = cli.clientParam().getPlatform();
            version = cli.clientParam().getVersion();
            ditchId = null==cli.clientParam().getChannelId()?"":cli.clientParam().getChannelId().toString();
        }

        if(log.isInfoEnabled()){
            log.info("RechargeMoney it's parameter: parentRechargeTypeId="+parentRechargeTypeId
                    +", paltformId="+platformId+", version="+version+", ditchId="+ditchId);
        }


//        platformId=1;
//        version = "1.30";
//        ditchId="3";
//        log.info("====RechargeMoney it's parameter: parentRechargeTypeId="+parentRechargeTypeId
//                +", paltformId="+platformId+", version="+version+", ditchId="+ditchId);



        //从Redis中获取指定的支付方式,其中包含支付配置信息,这个方法没考虑二级支付方式
        PaySwitchBaseDto paySwitchBaseDto = paySwitchGeneralService.getPaySwitchBaseDto(platformId, version, ditchId, parentRechargeTypeId);

        //支付方式名称
        String paySwitchName = null;
        //支付类型:1 360充值,2支付宝充值,3移动短信充值,4联通短信充值,5电信短信充值
        Integer payTypeId = -1;
        //温馨提示
        String tips = null;
        //获取支付方式下的支付配置
        List<PayConfigBase> payConfigList = null;
        if(null!=paySwitchBaseDto){
            paySwitchName = paySwitchBaseDto.getName();
            payTypeId = paySwitchBaseDto.getPayTypeId();
            tips = paySwitchBaseDto.getTips();

            if(null!=paySwitchBaseDto.getPayConfigBaseList()){
                //这里不考虑二级支付,所以直接获取一级支付下的支付配置
                payConfigList = paySwitchBaseDto.getPayConfigBaseList();
            }else{
                payConfigList = Lists.newArrayList();

                if(log.isInfoEnabled()){
                    log.info("Non RechargeConfig.......");
                }
            }
        }else{
            if(log.isInfoEnabled()){
                log.info("Non RechargeConfig, the paySwitchBaseDto is null .......");
            }
        }

        //支付类型必须大于0
        Preconditions.checkArgument(null!=payTypeId && payTypeId>0, "The PayTypeId muse be greater than 0!!!");

        //如果支付类型为移动、联通或电信短信类型,则href有值
        String href = null;
        if(null!=payTypeId && (payTypeId==3 || payTypeId==4 || payTypeId==5)){
            href = "/ft/recharge/rechargePhoneMessage.do?parentRechargeTypeId="+parentRechargeTypeId;
        }

        if(log.isInfoEnabled()){
            log.info("RechargeMoney paySwitchName is : " + paySwitchName);
            log.info("RechargeMoney payTypeId is : " + payTypeId);
            log.info("RechargeMoney tips is : " + tips);
            log.info("RechargeMoney href is : " + href);
        }

        //支付方式名称
        model.put("paySwitchName", paySwitchName);
        //支付类型:1 360充值,2支付宝充值,3移动短信充值,4联通短信充值,5电信短信充值
        model.put("payTypeId", payTypeId);
        //温馨提示
        model.put("tips", tips);
        //支付配置
        model.put("payConfigList", payConfigList);
        //跳转的href
        model.put("href", href);

        return new ModelAndView("/pages/recharge-money");
    }

    /**
     * Description 书籍购买
     * @param request
     * @param response
     * @param uid 用户ID
     * @param bookId 书籍ID
     * @param buyChapterId 要购买的章节ID
     * @param platformId 平台ID
     * @param version 版本
     * @param ditchId 渠道ID
     * @param model
     * @return
     */
    @RequestMapping("bookOrder.do")
    public ModelAndView bookOrder(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam(value = "uid", required = true) Integer uid,
                                     @RequestParam(value = "bookId", required = true) Integer bookId,
                                     @RequestParam(value = "buyChapterId", required = true) Integer buyChapterId,
                                     @RequestParam(value = "platformId", required = true) Integer platformId,
                                     @RequestParam(value = "version", required = true) String version,
                                     @RequestParam(value = "ditchId", required = false) String ditchId,
                                     ModelMap model) {

        if(null==uid || null==platformId || null==version || null==ditchId){
            ClientInfo cli = (ClientInfo) request.getAttribute("xclient");
            if(null!=cli){
                uid = cli.getUid();
                platformId = cli.clientParam().getPlatform();
                version = cli.clientParam().getVersion();
                ditchId = null==cli.clientParam().getChannelId()?"":cli.clientParam().getChannelId().toString();
            }
        }

        if(log.isInfoEnabled()){
            log.info("BookBorder it's parameter: uid="+uid+", bookId="+bookId+", buyChapterId="+buyChapterId
                    +", paltformId="+platformId+", version="+version+", ditchId="+ditchId);
        }

        User user = userService.get(uid);
        model.put("user", user);

        model.put("platformId", platformId);
        model.put("version", version);
        model.put("ditchId", ditchId);

        model.put("bookId", bookId);
        model.put("buyChapterId", buyChapterId);

        Integer dayNum = subConfigGeneralService.getSubConfig();
        //自动购买的天数
        model.put("dayNum", dayNum);
        if(log.isInfoEnabled()){
            log.info("The auto buy dayNum is : "+dayNum);
        }

        //获取书籍信息
        BookBase bookBase = bookGeneralService.get(Long.valueOf(bookId));
        String bookName = null;
        Integer wholePrice = null;
        //书籍是否收费、书籍是否全本收费
        Integer bookIsFee = null;
        Integer bookIsWholeFee = null;
        if(null!=bookBase){
            bookName = bookBase.getName();
            wholePrice = bookBase.getWholePrice();
            bookIsFee = bookBase.getIsFee();
            bookIsWholeFee = bookBase.getIsWholeFee();
        }
        model.put("bookName", bookName);
        model.put("wholePrice", wholePrice);
        model.put("bookIsFee", bookIsFee);
        model.put("bookIsWholeFee", bookIsWholeFee);
        if(log.isInfoEnabled()){
            log.info("The book info: bookName="+bookName+", wholePrice="+wholePrice
                    +", bookIsFee="+bookIsFee+", bookIsWholeFee="+bookIsWholeFee);
        }
        //获取章节信息
        Chapter chapter = chapterGeneralService.get(Long.valueOf(buyChapterId));
        String chapterName = null;
        Integer chapterWords = null;
        Integer chapterPrice = null;
        if(null!=chapter){
            chapterName = chapter.getName();
            chapterWords = chapter.getWords();
            chapterPrice = chapter.getPrice();
        }
        model.put("chapterName", chapterName);
        model.put("chapterWords", chapterWords);
        model.put("chapterPrice", chapterPrice);
        if(log.isInfoEnabled()){
            log.info("The chapter info: chapterName="+chapterName+", chapterWords="+chapterWords+", chapterPrice="+chapterPrice);
        }

        return new ModelAndView("/pages/book-order");
    }

    @RequestMapping("rechargeResult.do")
    public ModelAndView rechargeResult(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @RequestParam(value = "uid", required = true) Integer uid,
                                  @RequestParam(value = "merTradeCode", required = true) String merTradeCode,
                                  @RequestParam(value = "isSuccess", required = true) Integer isSuccess,
                                  @RequestParam(value = "platformId", required = true) Integer platformId,
                                  @RequestParam(value = "version", required = true) String version,
                                  @RequestParam(value = "ditchId", required = false) String ditchId,
                                  ModelMap model) {

        if(log.isInfoEnabled()){
            log.info("RechargeResult it's parameter: uid="+uid+", merTradeCode="+merTradeCode+", isSuccess="+isSuccess
                    +", paltformId="+platformId+", version="+version+", ditchId="+ditchId);
        }

        User user = userService.get(uid);
        model.put("user", user);

        model.put("platformId", platformId);
        model.put("version", version);
        model.put("ditchId", ditchId);

        model.put("isSuccess", isSuccess);

        //结果也,默认跳到recharge-result.html
        String resultPage = "/pages/recharge-result";

        if(null!=isSuccess && isSuccess==1){
            //成功,根据订单号更新订单状态
            rechargeService.clientDoneUpdate(merTradeCode, Constants.RechStatus.clientDone);
        }else if(null!=isSuccess && isSuccess==0){
            //失败,根据订单号更新订单状态
            rechargeService.clientDoneUpdate(merTradeCode, Constants.RechStatus.clientFail);
        }else{
            //跳到错误页
            resultPage = "/pages/error";
        }

        return new ModelAndView(resultPage);
    }

    @RequestMapping("rechargePhoneMessage.do")
    public ModelAndView rechargePhoneMessage(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @RequestParam(value = "parentRechargeTypeId", required = true) Integer parentRechargeTypeId,
                                       @RequestParam(value = "payConfigId", required = true) Integer payConfigId,
                                       ModelMap model) {

        //平台ID,版本,渠道号
        Integer platformId = -1;
        String version = "";
        String ditchId = "";

        ClientInfo cli = (ClientInfo) request.getAttribute("xclient");
        if(null!=cli && null!=cli.clientParam()){
            platformId = cli.clientParam().getPlatform();
            version = cli.clientParam().getVersion();
            ditchId = null==cli.clientParam().getChannelId()?"":cli.clientParam().getChannelId().toString();
        }

        if(log.isInfoEnabled()){
            log.info("RechargePhoneMessage it's parameter: parentRechargeTypeId="+parentRechargeTypeId
                    +", paltformId="+platformId+", version="+version+", ditchId="+ditchId);
        }

        //从Redis中获取指定的支付方式,其中包含支付配置信息(不能使用parentRechargeTypeId直接从DB从取,因为这时支付方式在数据库中的tips
        // 和支付方式在Redis中的tips有可能不同步,即修改后还未同步到Redis中)
        PaySwitchBaseDto paySwitchBaseDto = paySwitchGeneralService.getPaySwitchBaseDto(platformId, version, ditchId, parentRechargeTypeId);

        //支付方式名称
        String paySwitchName = null;
        //支付类型:1 360充值,2支付宝充值,3移动短信充值,4联通短信充值,5电信短信充值
        Integer payTypeId = -1;
        //温馨提示
        String tips = null;
        if(null!=paySwitchBaseDto){
            paySwitchName = paySwitchBaseDto.getName();
            payTypeId = paySwitchBaseDto.getPayTypeId();
            tips = paySwitchBaseDto.getTips();
        }else{
            if(log.isInfoEnabled()){
                log.info("RechargePhoneMessage, the paySwitchBaseDto is null, So not paySwitchName, payTypeId and tips!!!");
            }
        }

        //用户ID
        Integer uid = null;
        if(null!=cli) uid = cli.getUid();
        //获取用户上次充值成功的手机号
        String telNum = null;
        if(null!=uid && uid>0){
            RechargeQueryDto rechargeQueryDto = new RechargeQueryDto();
            rechargeQueryDto.setUserId(uid);
            //6服务端支付成功
            rechargeQueryDto.setRechargeStatus(6);
            //支付类型:1 360充值,2支付宝充值,3移动短信充值,4联通短信充值,5电信短信充值
            List<Integer> list = Lists.newArrayList(payTypeId);
            rechargeQueryDto.setPayTypeList(list);
            //按创建时间降序排列
            rechargeQueryDto.setOrderType(2);

            Page<RechargeCode> page = rechargeRepository.getPage(rechargeQueryDto,RechargeCode.class);

            if(null!=page && CollectionUtils.isNotEmpty(page.getResult())){
                telNum = page.getResult().get(0).getMobile();
            }else{
                if(log.isInfoEnabled()){
                    log.info("RechargePhoneMessage, It isn't success RechargeInfo!!!!!");
                }
            }
        }

        if(log.isInfoEnabled()){
            log.info("RechargePhoneMessage paySwitchName is : " + paySwitchName);
            log.info("RechargePhoneMessage parentRechargeTypeId is : " + parentRechargeTypeId);
            log.info("RechargePhoneMessage payTypeId is : " + payTypeId);
            log.info("RechargePhoneMessage tips is : " + tips);
            log.info("RechargePhoneMessage payConfigId is : " + payConfigId);
            log.info("RechargePhoneMessage userId is : " + uid);
            log.info("RechargePhoneMessage telNum is : " + telNum);
        }

        model.put("payTypeId", payTypeId);
        model.put("paySwitchName", paySwitchName);
        model.put("tips", tips);
        model.put("payConfigId", payConfigId);
        model.put("telNum", telNum);

        return new ModelAndView("/pages/recharge-phone-message");
    }


    @RequestMapping("rechargePhoneMessageCodeVerify.do")
    public ModelAndView rechargePhoneMessageCodeVerify(HttpServletRequest request,
                                             HttpServletResponse response,
                                             @RequestParam(value = "merTradeCode", required = true) String merTradeCode,
                                             @RequestParam(value = "payConfigId", required = true) Integer payConfigId,
                                             ModelMap model) {
        //平台ID,版本,渠道号
        Integer platformId = -1;
        String version = "";
        String ditchId = "";

        ClientInfo cli = (ClientInfo) request.getAttribute("xclient");
        if(null!=cli && null!=cli.clientParam()){
            platformId = cli.clientParam().getPlatform();
            version = cli.clientParam().getVersion();
            ditchId = null==cli.clientParam().getChannelId()?"":cli.clientParam().getChannelId().toString();
        }

        if(log.isInfoEnabled()){
            log.info("RechargeCodeVerify merTradeCode is : " + merTradeCode);
            log.info("RechargeCodeVerify payConfigId is : " + payConfigId);

            log.info("RechargeCodeVerify it's parameter: paltformId="+platformId+", version="+version+", ditchId="+ditchId);
        }

        //根据ID从DB中获取支付配置
        PayConfig payConfig = paySwitchGeneralService.getPayMoney(payConfigId);
        //充值金额
        Integer amount = null;

        //支付方式名称
        String paySwitchName = null;
        //温馨提示
        String tips = null;

        if(null!=payConfig){
            amount = payConfig.getAmount();

            // 从Redis中获取支付方式(不能从DB中获取,因为有可能因修改后未发布从而导致DB和Redis
            // 中数据不同步),这里的支付方式有可能是一级的,也有可能是二级的.
            PaySwitchBaseDto paySwitchBaseDto = null;
            //获取支付配置对应的支付方式的ID
            Integer parentId = null;
            Integer childId = null;
            //一级支付,不存在二级支付
            if(null!=payConfig.getChildPaySwitchId() && payConfig.getChildPaySwitchId()<=0){
                parentId = payConfig.getParentPaySwitchId();
                paySwitchBaseDto = paySwitchGeneralService.getPaySwitchBaseDto(platformId, version, ditchId, parentId);
            }else{//二级支付
                parentId = payConfig.getParentPaySwitchId();
                childId = payConfig.getChildPaySwitchId();
                paySwitchBaseDto = paySwitchGeneralService.getChildPaySwitchBaseDto(platformId, version, ditchId, parentId, childId);
            }

            if(null!=paySwitchBaseDto){
                paySwitchName = paySwitchBaseDto.getName();
                tips = paySwitchBaseDto.getTips();
            }else{
                if(log.isInfoEnabled()){
                    log.info("RechargeCodeVerify, the paySwitchBaseDto is null, So not paySwitchName and tips!!!");
                }
            }
        }else{
            if(log.isInfoEnabled()){
                log.info("RechargeCodeVerify, The PayConfig is NUll!!!!!");
            }
        }

        String telNum = null;
        if(null!=merTradeCode){
            RechargeQueryDto rechargeQueryDto = new RechargeQueryDto();
            rechargeQueryDto.setMerTradeCode(merTradeCode);
            Page<RechargeCode> page = rechargeRepository.getPage(rechargeQueryDto,RechargeCode.class);

            if(null!=page && CollectionUtils.isNotEmpty(page.getResult())){
                telNum = page.getResult().get(0).getMobile();
            }else{
                if(log.isInfoEnabled()){
                    log.info("RechargeCodeVerify, The merTradeCode["+merTradeCode+"] hasn't Info!!!1");
                }
            }
        }

        if(log.isInfoEnabled()){
            log.info("RechargeCodeVerify paySwitchName is : " + paySwitchName);
            log.info("RechargeCodeVerify tips is : " + tips);
            log.info("RechargeCodeVerify amount is : " + amount);
            log.info("RechargeCodeVerify telNum is : " + telNum);
        }

        model.put("paySwitchName", paySwitchName);
        model.put("telNum", telNum);
        model.put("amount", amount);
        model.put("merTradeCode", merTradeCode);
        model.put("payConfigId", payConfigId);
        model.put("tips", tips);

        return new ModelAndView("/pages/recharge-phone-message-code-verify");
    }
}
