package kr.co.shop.web.product.model.master;

import kr.co.shop.common.request.FileUpload;
import kr.co.shop.web.product.model.master.base.BaseBdProductReviewImage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class BdProductReviewImage extends BaseBdProductReviewImage {

	/** 파일 */
	private FileUpload imageFile;

}
