package com.hnair.consumer.wallet.api;

import com.hnair.consumer.wallet.model.RechargeDetail;
import com.hnair.consumer.wallet.vo.WalletMessageVo;

import java.math.BigDecimal;
import java.util.List;

public interface IRechargeDetailApi {

    /**
     * 提交线下充值申请接口
     * @param
     * @return
     */
    public WalletMessageVo createRecharge(String token, BigDecimal amount, String serialNumber,String tradingAccountName,String tradingAccountNumber,String applyUid, String applyUserName,String applyUserPhone, String reqInfo);


    /**
     * 充值历史记录
     * @param
     * @return
     */
    public WalletMessageVo<List<RechargeDetail>> queryRechargeHistory(String token,Integer pageNumber,Integer pageSize,RechargeDetail rechargeDetail);

    /**
     * 财务充值申请审核列表
     * @return
     */
    public WalletMessageVo<List<RechargeDetail>> queryRechargeAuditList(String token,Integer pageNumber,Integer pageSize,RechargeDetail rechargeDetail);

    /**
     * 财务充值申请审核
     * @param
     * @return
     */
    public WalletMessageVo rechargeAudit(String token,Long id,Integer state,String auditUid,String auditUserName,String inspectInfo);
}
