package com.rowem.passipadcloud;

public class InterfaceCodes {
	
	//////////////////////
	// 공통사용 코드.
	
	/**
	 * 오류 없음.
	 */
	public static final int ERR_OK			= 0;
	
	/**
	 * S pinpad 서비스 사용자.
	 */
	public static final int ERR_REG_USER	= 30;
	
	/**
	 * S pinpad 서비스 비가입자.
	 */
	public static final int ERR_NON_USER	= 31;
	
	/**
	 * S pinpad 재가입 필요.
	 */
	public static final int ERR_NEED_JOIN	= ERR_REG_USER * -1;
	
	
	//////////////////////
	// 다이얼로그에서 비밀번호 입력 했을 때 발생하는 값들...
	/**
	 * 입력값이 모자람.
	 */
	public static final int ERR_TOO_SHORT	= 1;
	
	/**
	 * <pre>
	 * 서비스 가입시 흐름상 사용하고자 하는 비밀번호를 두번 입력하는데 
	 * 처음 입력값과 나중 입력값이 다름.
	 * </pre>
	 */
	public static final int ERR_NOT_MATCH	= 2;
	
	
	///////////////////////
	// PkiUtil 라이브러리가 통신하다가 생기는 오류 코드.
	/**
	 * 통신에는 성공 했으나 원하는 처리결과는 얻지 못했다.
	 */
	public static final int ERR_PARAMS		= 10;
	
	/**
	 * 로그인 시 비밀번호 오류...
	 */
	public static final int ERR_INVALID_PW	= 11;
	
	/**
	 * 통신 자체가 실패.
	 */
	public static final int ERR_CONNECT		= 90;
}
