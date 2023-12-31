package com.db.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.db.model.AuctionVO;
import com.db.model.CartVO;
import com.db.model.CouponVO;
import com.db.model.OrderVO;
import com.db.model.ProductVO;
import com.db.model.UserVO;
import com.db.service.ProductService;
import com.db.service.UserService;

@Controller
@RequestMapping(value = "/product")
public class ProductController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	ProductService productService;

	@Autowired
	UserService userService;
	
	// 브랜드 상품 리스트 페이지 이동
	@GetMapping("brandProductList")
	public void brandProductListGET(String bname, HttpServletRequest request) {
		logger.info("브랜드 상품 리스트 페이지 진입");
		request.setAttribute("bname", bname);

		try {
			ArrayList<ProductVO> bplist = productService.brandProductList(bname);
			request.setAttribute("bplist", bplist); // 브랜드명으로 상품을 불러옴
			
		} catch (Exception e) {

			e.printStackTrace();
		}

	}
	
	// 브랜드 카테고리 상품 리스트
	@GetMapping("/categoriesList")
	public String categoriesListGET(String bname, int kind, HttpServletRequest request) throws Exception {
		
		request.setAttribute("bname", bname); 
		
		ArrayList<ProductVO> bplist = productService.categoriesList(bname, kind);
		request.setAttribute("bplist", bplist);
		
		return "/product/brandProductList";
	}

	// 상품 검색
	@GetMapping("/searchProduct")
	public String searchProductGET(String pname, HttpServletRequest request) {
		request.setAttribute("pname", pname);
		try {
			ArrayList<ProductVO> bplist = productService.searchProduct(pname);
			if (bplist == null || bplist.isEmpty()) {
				return "/product/searchNotFound";
			}
			request.setAttribute("bplist", bplist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 상품 상세 보기
	@GetMapping("/productDetail")
	public String productDetailGET(int num, HttpServletRequest request) throws Exception {
		ProductVO pdlist = productService.productDetail(num);
		String pname = request.getParameter("pname");
		List<ProductVO> slist = productService.productSizeList(pname);

		request.setAttribute("ps", slist); // 상품 이름으로 존재하는 사이즈를 가져옴
		request.setAttribute("pdlist", pdlist); // 상품번호로 상품정보를 가져옴
		return "/product/productDetail";

	}

	// 장바구니에 상품 추가
	@PostMapping("/addCart")
	@ResponseBody
	public ResponseEntity<?> addCartPOST(String userid, CartVO cart, HttpServletRequest request, HttpSession session)
			throws Exception {

		productService.addCart(cart); // 장바구니 추가 쿼리 실행

		return ResponseEntity.ok("Success");
	}

	// 나의 장바구니
	@GetMapping("/myCart")
	public String myCartGET(String userid, HttpServletRequest request, HttpSession session) throws Exception {

		ArrayList<CartVO> clist = productService.getCartList(userid); // userid로 장바구니 리스트 불러오기
		request.setAttribute("clist", clist);

		ArrayList<ProductVO> plist = productService.getAllProduct(); // 모든 상품 가져오기
		request.setAttribute("plist", plist);

		return "/product/cart";

	}

	// 장바구니 뱃지 상품 개수
	@GetMapping("/countCartAjax")
	@ResponseBody
	public String countCartAjax(String userid) throws Exception {
		return String.valueOf(productService.countCart(userid));
	}

	// 세일 상품 리스트
	@GetMapping("/saleList")
	public String hotDealListGET(HttpServletRequest request, RedirectAttributes rttr) {

		try {
			ArrayList<ProductVO> plist = productService.getAllProductNoDup(); // 모든 상품 중복없이 가져오기
			request.setAttribute("hdlist", plist);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return "/product/saleList";

	}

	// 장바구니 수량 감소
	@PostMapping("/quantityMinus")
	public ResponseEntity<String> decreaseQuantity(@RequestParam("cartnum") int cartnum) throws Exception {
		productService.decreaseQuantity(cartnum);
		return ResponseEntity.ok("Success");
	}

	// 장바구니 수량 증가
	@PostMapping("/quantityPlus")
	@ResponseBody
	public ResponseEntity<String> increaseQuantity(@RequestParam("cartnum") int cartnum) throws Exception {
		productService.increaseQuantity(cartnum);
		return ResponseEntity.ok("Success");
	}

	// 장바구니 상품 삭제
	@PostMapping("/deleteCart")
	@ResponseBody
	public ResponseEntity<String> cartDelete(@RequestParam("cartnum") int cartnum) throws Exception {
		productService.cartDelete(cartnum);
		return ResponseEntity.ok("Success");
	}

	// 체크아웃 페이지
	@GetMapping("/checkOut")
	public String checkOutGET(String userid, HttpServletRequest request, HttpSession session) throws Exception {

		ArrayList<CartVO> clist = productService.getCartList(userid); // userid로 장바구니 리스트 불러오기
		request.setAttribute("clist", clist);

		ArrayList<ProductVO> plist = productService.getAllProduct(); // 모든 상품 가져오기
		request.setAttribute("plist", plist);

		ArrayList<CouponVO> couplist = userService.getMyCoupon(userid); // 쿠폰 정보 불러오기
		request.setAttribute("couplist", couplist);
		return "/product/checkOut";
	}

	// 결제완료
	@PostMapping("/purchased")
	public String purchasedPOST(@Param("cnum") Integer cnum, @Param("cartnum") int cartnum,
			@Param("userid") String userid, @Param("totalprice") int totalprice, @Param("name") String name,
			@Param("email") String email, @Param("phone") String phone, @Param("address1") String address1,
			@Param("address2") String address2, @Param("address3") String address3, HttpServletRequest request)
			throws Exception {

		if (cnum != null) {
			productService.useCoupon(cnum); // 쿠폰 적용후 쿠폰 result값 변경
		}
		int orderNumber = productService.getLatestOrderNumber(userid);// orderNumber를 가져옴

		ArrayList<CartVO> cartlist = productService.getCartList(userid);
		for (CartVO cart : cartlist) {
			productService.addOrderDetail(cart, totalprice, orderNumber, name, phone, email, address1, address2,
					address3); // order_detail table에 저장
			productService.cartResultChange(cartnum, cart);
			// 주문완료한 장바구니 result -> 0 으로 변경

		}

		ArrayList<ProductVO> plist = productService.getAllProduct();
		request.setAttribute("plist", plist); // 상품정보 불러오고 저장

		ArrayList<OrderVO> olist = productService.getOrderList(orderNumber);
		request.setAttribute("olist", olist); // 마지막 주문정보를 불러오고 저장

		return "/product/orderList";
	}

	// 옥션 체크아웃
	@GetMapping("/auctionCheckOut")
	public void auctionCheckOutGET(int auNum, Model model) throws Exception {
		AuctionVO auVo = productService.getAuctionDetail(auNum);
		model.addAttribute("auVo", auVo);
	}

	// 옥션 결제완료
	@PostMapping("auctionPurchased")
	public String auctionPurchasedPOST(CartVO cart, UserVO uVo, int totalprice, int auNum, int endPrice, Model model)
			throws Exception {
		System.out.println("auctionPurchased 실행");
		AuctionVO auVo = productService.getAuctionDetail(auNum);
		String param1 = auVo.getPname();
		String param2 = auVo.getPsize();
		// System.out.println("pname/psize"+ param1+"/"+param2);
		cart.setNum(productService.productDetailByPnamepSize(param1, param2).getNum()); // cart num 수정
		cart.setResult(0);
		// productService.addCart(cart);
		// System.out.println("endPrice: "+endPrice);
		int arg1 = auNum;
		int arg0 = endPrice;
		productService.setAuctionEndPrice(arg0, arg1); // 옥션 endPrice 설정
		int orderNumber = productService.getLatestOrderNumber(uVo.getUserid());// orderNumber를 가져옴

		productService.addOrderDetail(cart, totalprice, orderNumber, uVo.getName(), uVo.getPhone(), uVo.getEmail(),
				uVo.getAddress1(), uVo.getAddress2(), uVo.getAddress3());

		// productService.cartResultChange(cart.getCartnum(), cart);

		ArrayList<ProductVO> plist = productService.getAllProduct();
		model.addAttribute("plist", plist); // 상품정보 불러오고 저장

		ArrayList<OrderVO> olist = productService.getOrderList(orderNumber);
		model.addAttribute("olist", olist); // 마지막 주문정보를 불러오고 저장

		return "/product/orderList";
	}

	// 옥션 리스트 페이지
	@GetMapping("/auctionView")
	public void auctionViewGET(Model model) throws Exception {
		System.out.println("auctionView 접속");
		ArrayList<AuctionVO> auVo = productService.getAuctionList();
		for (AuctionVO vo : auVo) {
			if (vo.getEndTime().before(new Date()) && vo.getOnOff() == 1) {
				System.out.println("auctionView에서의 경매종료 매서드 작동");
				productService.endAuction(vo.getNum());
			}
		}
		model.addAttribute("AuctionList", auVo);
	}

	// 옥션 상세 페이지
	@GetMapping("auctionDetail")
	public void auctionDetailGET(int num, String pName, Model model) throws Exception {
		System.out.println("auctionDetail 접속");
		AuctionVO auVo = productService.getAuctionDetail(num);
		ProductVO pVo = productService.productDetailByPname(pName);

		model.addAttribute("originProduct", pVo);
		model.addAttribute("auction", auVo);
	}

	// 옥션 입찰
	@PostMapping("dealAuction.do")
	public String auctionEnrollPOST(AuctionVO auVo, String originProduct, Model model) throws Exception {
		System.out.println("dealAuction.do 실행");
		System.out.println("dealAuction auVo: " + auVo);
		productService.dealAuction(auVo);
		model.addAttribute("pName", originProduct);
		model.addAttribute("num", auVo.getNum());
		return "redirect:/product/auctionDetail";
	}

	// 옥션 기간 만료시
	@PostMapping("expiredAuction.do")
	public void expiredAuctionPOST(int num) throws Exception {
		System.out.println("expiredAuction.do 실행");
		productService.endAuction(num);
	}

	// 옥션 가격 실시간 업데이트
	@PostMapping("getPrice")
	@ResponseBody
	public String getPricePOST(int num, Model model) throws Exception {
		// System.out.println("getPrice 실행");
		// System.out.println("num: "+num);
		int price1 = productService.getAuctionDetail(num).getPrice();
		String price = Integer.toString(price1);
		// System.out.println("price: "+price);
		return price;
	}
}
