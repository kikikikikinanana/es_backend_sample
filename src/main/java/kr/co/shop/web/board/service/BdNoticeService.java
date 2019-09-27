package kr.co.shop.web.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.shop.common.paging.Pageable;
import kr.co.shop.web.board.exception.BdNoticeException;
import kr.co.shop.web.board.model.master.BdNotice;
import kr.co.shop.web.board.repository.master.BdNoticeDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BdNoticeService {

	@Autowired
	private BdNoticeDao noticeDao;

	/**
	 * @Desc : 공지사항 메인페이지 호출
	 * @Method Name : getNoticeMain
	 * @Date : 2019. 3. 27.
	 * @Author : 신인철
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getNoticeMain(Pageable<BdNotice, BdNotice> pageable) throws Exception {
		Map<String, Object> noticeMap = new HashMap<>();

		int totalCount = noticeDao.selectNoticeCount(pageable);

		if (totalCount > 0) {
			noticeMap.put("content", noticeDao.selectNoticeList(pageable));
		}
		noticeMap.put("totalCount", totalCount);

		return noticeMap;
	}

	/**
	 * @Desc : 공지사항 상세보기 호출
	 * @Method Name : getNoticeDetail
	 * @Date : 2019. 3. 27.
	 * @Author : 신인철
	 * @param bdNotice
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getNoticeDetail(BdNotice bdNotice) throws Exception {
		Map<String, Object> rsMap = new HashMap<String, Object>();
		BdNotice noticeDetail = noticeDao.selectNoticeDetail(bdNotice);

		if (noticeDetail != null) {
			rsMap.put("noticeDetail", noticeDetail);
			rsMap.put("prevDetail", noticeDao.selectPrevNoticeDetail(bdNotice));
			rsMap.put("nextDetail", noticeDao.selectNextNoticeDetail(bdNotice));
		} else {
			throw new BdNoticeException("유효하지 않은 페이지입니다.");
		}

		return rsMap;
	}

	/**
	 * @Desc : 푸터 공지사항 노출
	 * @Method Name : getFooterNotice
	 * @Date : 2019. 5. 7.
	 * @Author : 신인철
	 * @return
	 * @throws Exception
	 */
	public List<BdNotice> getFooterNotice() throws Exception {
		return noticeDao.selectFooterNotice();
	}

	/**
	 * @Desc : MO 고객센터 메인 공지사항 노출
	 * @Method Name : getNoticeCsMain
	 * @Date : 2019. 5. 10.
	 * @Author : 신인철
	 * @return
	 * @throws Exception
	 */
	public List<BdNotice> getNoticeCsMain() throws Exception {
		return noticeDao.selectNoticeCsMain();
	}

}
