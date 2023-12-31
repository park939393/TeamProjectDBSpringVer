package com.db.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.mapper.AdminMapper;
import com.db.model.AuctionVO;
import com.db.model.BrandVO;
import com.db.model.Criteria;
import com.db.model.OrderVO;
import com.db.model.ProductVO;
import com.db.model.UserVO;

@Service
public class AdminServiceImpl implements AdminService{
	
	@Autowired
	AdminMapper mapper;

	@Override
	public int insertAuction(AuctionVO aVo) throws Exception {
		return mapper.insertAuction(aVo);
	}

	@Override
	public ArrayList<UserVO> getUserList() throws Exception {
		return mapper.getUserList();
	}

	@Override
	public void deleteUser(String userid) throws Exception {
		mapper.deleteUser(userid);
	}

	@Override
	public UserVO getUser(String userid) throws Exception {
		return mapper.getUser(userid);
	}

	@Override
	public void adminUserUpdate(UserVO uVo) throws Exception {
		mapper.adminUserUpdate(uVo);
	}

	@Override
	public void brandEnroll(BrandVO bVo) throws Exception {
		mapper.brandEnroll(bVo);		
	}

	@Override
	public void deleteBrand(String bname) throws Exception {
		mapper.deleteBrand(bname);
	}

	@Override
	public ArrayList<OrderVO> getNewOrder() throws Exception {
	
		return mapper.getNewOrder();
	}

	@Override
	public ArrayList<OrderVO> getProcessOrder() throws Exception {
		
		return mapper.getProcessOrder();
	}

	@Override
	public ArrayList<OrderVO> getWithdrawOrder() throws Exception {
		
		return mapper.getWithdrawOrder();
	}

	@Override
	public int shipmentChangeResult(int ordernumber) {
		
		return mapper.shipmentChangeResult(ordernumber);
				
	}

	@Override
	public int checkOrderChangeResult(int ordernumber) {
	
		return mapper.checkOrderChangeResult(ordernumber);
	}

	@Override
	public int withdrawOrderChangeResult(int ordernumber) {
		
		return mapper.withdrawOrderChangeResult(ordernumber);
	}

	@Override
	public ArrayList<OrderVO> getSalesOrder() throws Exception {
	
		return mapper.getSalesOrder();
	}

	@Override
	public List<UserVO> getUserListPaging(Criteria cri) throws Exception {
		return mapper.getUserListPaging(cri);
	}

	@Override
	public int getUserTotal(Criteria cri) throws Exception {
		return mapper.getUserTotal(cri);
	}
	@Override
	public List<ProductVO> getProductList(Criteria cri) {
		return mapper.getProductList(cri);
	}

	@Override
	public List<BrandVO> getBrandList(Criteria cri) {
		return mapper.getBrandList(cri);
	}

	@Override
	public int productGetTotal(Criteria cri) {
		return mapper.productGetTotal(cri);
	}

	@Override
	public int brandGetTotal(Criteria cri) {
		return mapper.brandGetTotal(cri);
	}

	@Override
	public void productEnroll(ProductVO product) {
		mapper.productEnroll(product);

	}

	@Override
	public ProductVO productGetDetail(int num) {
		System.out.println("(service) productGetDetail ..... " + num);
		return mapper.productGetDetail(num);
	}

	@Override
	public int productDelete(int num) {
		System.out.println("(service) productDelete ..... " + num);
		return mapper.productDelete(num);
	}

	@Override
	public int productModify(ProductVO product) {
		System.out.println("(service) productModify ..... ");
		return mapper.productModify(product);
	}
	
}
