/**
 * 
 */
package kr.co.shop.web.member.exception;

/**
 * @Desc :
 * @FileName : MemberException.java
 * @Project : shop.backend
 * @Date : 2019. 3. 28.
 * @Author : Kimyounghyun
 */
public class MemberException extends Exception {

	private static final long serialVersionUID = 6709954193868603337L;

	public MemberException(String message) {
		super(message);
	}

	public static class InvalidCertificationNumberException extends MemberException {

		private static final long serialVersionUID = 3191299752182244552L;

		public InvalidCertificationNumberException(String message) {
			super(message);
		}

	}

	public static class LimitExceededCertificationNumber extends MemberException {

		private static final long serialVersionUID = -3676029253708795612L;

		public LimitExceededCertificationNumber(String message) {
			super(message);
		}

	}

	public static class NotCertificationNumberInfo extends MemberException {

		private static final long serialVersionUID = -3676029253708795612L;

		public NotCertificationNumberInfo(String message) {
			super(message);
		}

	}

}
