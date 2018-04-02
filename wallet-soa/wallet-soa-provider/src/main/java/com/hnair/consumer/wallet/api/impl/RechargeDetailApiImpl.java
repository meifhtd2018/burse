package com.hnair.consumer.wallet.api.impl;

import com.hnair.consumer.dao.service.ICommonService;
import com.hnair.consumer.dao.utils.PageFinder;
import com.hnair.consumer.dao.utils.Query;
import com.hnair.consumer.utils.DateUtil;
import com.hnair.consumer.wallet.api.IRechargeDetailApi;
import com.hnair.consumer.wallet.enums.WalletErrorCodeEnum;
import com.hnair.consumer.wallet.model.RechargeDetail;
import com.hnair.consumer.wallet.model.Wallet;
import com.hnair.consumer.wallet.model.WalletBillDetail;
import com.hnair.consumer.wallet.service.IWalletService;
import com.hnair.consumer.wallet.service.impl.WalletServiceImpl;
import com.hnair.consumer.wallet.utils.WalletUtils;
import com.hnair.consumer.wallet.vo.WalletMessageVo;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

import static com.hnair.consumer.dao.utils.ParameterUtils.convertBeanToMap;

@Component("rechargeDetailApi")
public class RechargeDetailApiImpl implements IRechargeDetailApi {

    @Resource(name = "walletCommonService")
    private ICommonService walletRechargeService;

    @Resource
    private IWalletService walletService;

    Logger logger = LoggerFactory.getLogger(RechargeDetailApiImpl.class);

    @Override
    public WalletMessageVo createRecharge(String token, BigDecimal amount, String serialNumber,String tradingAccountName,String tradingAccountNumber,String applyUid, String applyUserName,String applyUserPhone, String reqInfo) {
        WalletMessageVo result=new WalletMessageVo<>();
        WalletMessageVo<Long>  messageVo=walletService.getWalletIdByToken(token);
        if(!messageVo.isResult()){
            result.setErrorCode(messageVo.getErrorCode());
            result.setErrorMessage(messageVo.getErrorMessage());
            return result;
        }
        Long walletId=messageVo.getT();
        Wallet wallet = walletRechargeService.get(Wallet.class, "walletId",walletId);
        if(wallet == null){
            result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1001.getErrorCode().toString());
            result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1001.getErrorMessage());
            return result;
        }
        if(wallet.getState()==2){
            result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1022.getErrorCode().toString());
            result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1022.getErrorMessage());
            return result;
        }
        RechargeDetail rechargeDetail=new RechargeDetail();
        rechargeDetail.setWalletId(walletId);
        rechargeDetail.setAmount(amount);
        rechargeDetail.setSerialNumber(serialNumber);
        rechargeDetail.setTradingAccountName(tradingAccountName);
        rechargeDetail.setTradingAccountNumber(tradingAccountNumber);
        rechargeDetail.setApplyUsePhone(applyUserPhone);
        rechargeDetail.setApplyUid(applyUid);
        rechargeDetail.setApplyUserName(applyUserName);
        rechargeDetail.setCreateTime(new Date());
        rechargeDetail.setReqInfo(reqInfo);
        rechargeDetail.setModifyTime(new Date());
        rechargeDetail.setStatus(1);//审批中
        rechargeDetail.setBusinessNo(WalletUtils.buildToken());
        walletRechargeService.save(rechargeDetail);
        result.setResult(true);
        return result;
    }

    @Override
    public WalletMessageVo<List<RechargeDetail>> queryRechargeHistory(String token,Integer pageNumber,Integer pageSize,RechargeDetail rechargeDetail) {
        WalletMessageVo<List<RechargeDetail>> result=new WalletMessageVo<>();
        Query query = new Query();
        query.setPageSize(pageSize);
        query.setPage(pageNumber);
        WalletMessageVo<Long>  messageVo=walletService.getWalletIdByToken(token);
        if(!messageVo.isResult()){
            result.setErrorCode(messageVo.getErrorCode());
            result.setErrorMessage(messageVo.getErrorMessage());
            return result;
        }

        Long walletId=messageVo.getT();
        rechargeDetail.setWalletId(walletId);
        PageFinder<RechargeDetail> pageFinder= walletRechargeService.getPageFinder(RechargeDetail.class,query,convertBeanToMap(rechargeDetail));
        result.setResult(true);
        result.setCount(pageFinder.getRowCount());
        result.setT(pageFinder.getData());
        return result;
    }

    @Override
    public WalletMessageVo<List<RechargeDetail>> queryRechargeAuditList(String token,Integer pageNumber,Integer pageSize,RechargeDetail rechargeDetail) {
        WalletMessageVo<List<RechargeDetail>> result=new WalletMessageVo<>();
        Query query = new Query();
        query.setPageSize(pageSize);
        query.setPage(pageNumber);
        WalletMessageVo<Long>  messageVo=walletService.getWalletIdByToken(token);
        if(!messageVo.isResult()){
            result.setErrorCode(messageVo.getErrorCode());
            result.setErrorMessage(messageVo.getErrorMessage());
            return result;
        }
        PageFinder<RechargeDetail> pageFinder;
        if(rechargeDetail.getStatus()!=null&&rechargeDetail.getStatus()==4){//通过和未通过
            pageFinder= walletRechargeService.getPageFinder(rechargeDetail,query,"getAuditedPageCount","getAudited");
        }else{
            pageFinder=walletRechargeService.getPageFinder(rechargeDetail,query);
        }
        result.setResult(true);
        result.setCount(pageFinder.getRowCount());
        result.setT(pageFinder.getData());
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, transactionManager = "walletTransactionManager", isolation=Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public WalletMessageVo rechargeAudit(String token,Long id,Integer state,String auditUid,String auditUserName,String inspectInfo) {
        WalletMessageVo result=new WalletMessageVo<>();
        WalletMessageVo<Long>  messageVo=walletService.getWalletIdByToken(token);
        if(!messageVo.isResult()){
            result.setErrorCode(messageVo.getErrorCode());
            result.setErrorMessage(messageVo.getErrorMessage());
            return result;
        }
        RechargeDetail rechargeDetail=walletRechargeService.get(id,RechargeDetail.class);
        rechargeDetail.setStatus(state);//2通过 3未通过
        rechargeDetail.setAuditUid(auditUid);
        rechargeDetail.setAuditUserName(auditUserName);
        rechargeDetail.setInspectInfo(inspectInfo);
        rechargeDetail.setOldModifyTime(rechargeDetail.getModifyTime());
        rechargeDetail.setModifyTime(new Date());
        rechargeDetail.setInspectTime(new Date());
        int count=walletRechargeService.updateBySqlId("updateByPrimaryKeyUpdateTimeSelective",rechargeDetail);
       if(count==0){
           logger.info("updateByPrimaryKeyUpdateTimeSelective id:"+rechargeDetail.getId()+" OldModifyTime:"+rechargeDetail.getOldModifyTime());
           result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1016.getErrorCode().toString());
           result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1016.getErrorMessage());
           return result;
       }
        if(state==2){
            //金额变动
            Wallet wallet = walletRechargeService.get(Wallet.class, "walletId",rechargeDetail.getWalletId());

            wallet.setBalance(wallet.getBalance().add(rechargeDetail.getAmount()));
            wallet.setModifyTime(new Date());
            walletRechargeService.update(wallet);
            //记录钱包历史记录变更
            WalletBillDetail walletBillDetail = new WalletBillDetail();
            walletBillDetail.setWalletId(wallet.getWalletId());
            walletBillDetail.setTaskType(2);//2充值
            walletBillDetail.setAmount(rechargeDetail.getAmount());
            walletBillDetail.setAmountType(2);//收入
            walletBillDetail.setBusinessNo(rechargeDetail.getBusinessNo());
            walletBillDetail.setDescription(rechargeDetail.getInspectInfo());
            walletBillDetail.setCreateTime(DateUtil.getCurrentDateTime());
            walletRechargeService.save(walletBillDetail);
        }
        result.setResult(true);
        return result;
    }

}
