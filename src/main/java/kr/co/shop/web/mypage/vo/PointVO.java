package kr.co.shop.web.mypage.vo;

import kr.co.shop.common.bean.BaseBean;
import lombok.Data;

/**
 * 
 * @Desc :
 * @FileName : PointVO.java
 * @Project : shop.fo
 * @Date : 2019. 3. 13.
 * @Author : 유성민
 */
@Data
public class PointVO extends BaseBean {

	private static final long serialVersionUID = -5750418405166839655L;

	private String memberNo; // 회원번호
	private String pointPassword;
	private String pswdText;

}
