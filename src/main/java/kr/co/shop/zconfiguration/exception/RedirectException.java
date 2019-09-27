package kr.co.shop.zconfiguration.exception;

public class RedirectException extends RuntimeException {
	
	public RedirectException() {
		super();
	}
	
	public RedirectException(String message) {
		super(message);
	}
}
