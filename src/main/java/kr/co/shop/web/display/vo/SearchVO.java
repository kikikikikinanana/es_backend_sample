package kr.co.shop.web.display.vo;

import lombok.Data;

@Data
public class SearchVO {

	private String searchIP;

	private int searchPORT;

	private String aconnect;

	private String searchWord;

	private String[] researchWord;

	private String site;

	private String channel;

	private String[] gubun;

	private String[] category;

	private String[] brand;

	private String[] shoesSize;

	private String[] menSize;

	private String[] womenSize;

	private String[] kidsSize;

	private String[] color;

	private String[] price;

	private String[] etc;

	private String sort;

	private int page;

	private int perPage;

}
