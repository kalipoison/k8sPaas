package com.gohb.params.dto.voucher;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class VoucherDTO implements Serializable {
    /**
     * 兑换券ID
     */
    private Long voucherId;

    /**
     * 使用用户id
     */
    private Long userId;

    /**
     * 兑换码
     */
    private String voucherCode;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 状态  0：未使用   1：已使用
     */
    private Byte status;

    private static final long serialVersionUID = 1L;


}
