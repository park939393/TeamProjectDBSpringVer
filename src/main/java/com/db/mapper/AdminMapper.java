package com.db.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.db.model.AuctionVO;
import com.db.model.BrandVO;
import com.db.model.Criteria;
import com.db.model.OrderVO;
import com.db.model.ProductVO;
import com.db.model.UserVO;

public interface AdminMapper {

	public int insertAuction(AuctionVO aVo); // 옥션 등록

	public ArrayList<UserVO> getUserList(); // 유저 목록

	public void deleteUser(String userid); // 유저 삭제

	public UserVO getUser(String userid); // 유저 정보

	public void adminUserUpdate(UserVO uVo); // 유저 수정

	public void brandEnroll(BrandVO bVo); // 브랜드 추가

	public void deleteBrand(String bname); // 브랜드 삭제
	
	
	public List<ProductVO> getProductList(Criteria cri); // 등록된 상품 전체 가져오기

	public List<BrandVO> getBrandList(Criteria cri); // 등록된 브랜드 전체 가져오기

	public int productGetTotal(Criteria cri);// 등록된 상품의 수

	public int brandGetTotal(Criteria cri);// 등록된 브랜드의 수

	public void productEnroll(ProductVO product); // 상품 추가
	
	public ProductVO productGetDetail(int num); // 상품 정보 읽기
	
	public int productDelete(int num); // 선택 상품 삭제
	
	public int productModify(ProductVO product); // 선택 상품 수정

	// 신규주문 가져오기
	public ArrayList<OrderVO> getNewOrder();

	// 처리주문 가져오기
	public ArrayList<OrderVO> getProcessOrder();

	// 취소주문 가져오기
	public ArrayList<OrderVO> getWithdrawOrder();

	// 판매된주문 가져오기
	public ArrayList<OrderVO> getSalesOrder();

	// 주문 확인처리 result -> 2 로 변경
	public int checkOrderChangeResult(@Param("ordernumber") int ordernumber);

	// 주문 발송처리 result -> 3 으로 변경
	public int shipmentChangeResult(@Param("ordernumber") int ordernumber);

	// 주문 취소처리 result -> 5 으로 변경
	public int withdrawOrderChangeResult(@Param("ordernumber") int ordernumber);

	// 게시판 목록(페이징)
	public List<UserVO> getUserListPaging(Criteria cri);

	// 게시판 총 갯수
	public int getUserTotal(Criteria cri);
}
