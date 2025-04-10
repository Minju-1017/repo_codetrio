package com.codetrio.module.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrderController {
	
	private String path = "hoxdm/order/";
	
	@Autowired //인터페이스
	OrderService service;
	
	//유저
	@RequestMapping(value = "/hoxdm/order/UserOrderHOXdmList")
	public String UserOrderHOXdmList(Model model,@ModelAttribute("vo") OrderVo vo) {
		
		vo.setParamsPaging(service.selectOneCount(vo));
		
		if (vo.getTotalRows() > 0) {
			model.addAttribute("orderList", service.selectList(vo));
		}
		
		return path + "UserOrderHOXdmList"; 
	}
	
	@RequestMapping(value = "/hoxdm/order/UserOrderHOXdmForm")
	public String UserOrderHOXdmForm(@ModelAttribute("vo") OrderVo vo,
			Model model, OrderDto orderDto) throws Exception {
		
		if (vo.getoSeq().equals("0") || vo.getoSeq().equals("")) {
			// insert mode
		} else {
			List<OrderDto> dtoList = service.selectOne(orderDto); // 주문한 상품이 여러개일 수 있으므로, List로 읽는다
			OrderDto dto = new OrderDto();
			dto = dtoList.get(0);
			model.addAttribute("orderItem", dto);
			model.addAttribute("orderList", dtoList);
		}
		return path + "UserOrderHOXdmForm";
	}
	@RequestMapping(value = "/hoxdm/order/UserOrderProductHOXdmForm")
	public String UserOrderProductHOXdmForm(Model model) {
		model.addAttribute("productList",service.productList());
		return "hoxdm/order/UserOrderProductHOXdmForm";
	}
	@RequestMapping(value = "/hoxdm/order/UserOrderProductHOXdmInst")
	public String UserOrderProductHOXdmInst(OrderDto orderDto) {
		service.ordersInsert(orderDto);
		service.ordersProductInsert(orderDto);
		return "redirect:/hoxdm/order/UserOrderHOXdmList";
	}
	@ResponseBody
	@RequestMapping(value = "/hoxdm/order/UserOrderHOXdmUpdt")
	public Map<String, Object> UserOrderHOXdmUpdt(OrderDto orderDto, 
			@RequestParam(value="opSeqList") List<String> opSeqList,
			@RequestParam(value="opStateCdList") List<String> opStateCdList) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		if (orderDto == null || opSeqList == null || opStateCdList == null) {
			returnMap.put("rt", "fail");
		} else {
			int successCnt = service.update(orderDto);
			
			if (successCnt > 0) {
				if (opSeqList.size() > 0 && opStateCdList.size() > 0) {
					int opSuccessCnt = 0;
					
					for (int i = 0; i < opSeqList.size(); i++) {
						OrderDto dto = new OrderDto();
						
						dto.setOpSeq(opSeqList.get(i));
						dto.setOpStateCd(opStateCdList.get(i));
						
						opSuccessCnt = opSuccessCnt + service.updateOPList(dto);
					}
					
					if (opSuccessCnt > 0) {
						returnMap.put("rt", "success");	
					} else {
						returnMap.put("rt", "fail");
					}
				} else {
					returnMap.put("rt", "success");	// 수정할 데이터 없으므로 바로 성공
				}
			} else {
				returnMap.put("rt", "fail");
			}
		}

		return returnMap;
	}
	
	//거래처
	@RequestMapping(value = "/hoxdm/order/ClientOrderHOXdmList")
	public String ClientOrderHOXdmList(Model model,@ModelAttribute("vo") OrderVo vo) {
		
		vo.setParamsPaging(service.selectTwoCount(vo));
		
		if (vo.getTotalRows() > 0) {
			model.addAttribute("orderList", service.selectTwo(vo));
		}
		
		return path + "ClientOrderHOXdmList"; 
		}
	

	@RequestMapping(value = "/hoxdm/order/ClientOrderHOXdmInst")
	public String ClientOrderHOXdmInst(OrderDto orderDto) {
		
		service.insert(orderDto);
		return path + "ClientOrderHOXdmInst";
	}
	
	@RequestMapping(value = "/hoxdm/order/WarehouseOrderHOXdmList")
	public String WraehouseOrderHOXdmList(Model model,@ModelAttribute("vo") OrderVo vo) {
		
		vo.setParamsPaging(service.selectThreeCount(vo));
		
		if (vo.getTotalRows() > 0) {
			model.addAttribute("orderList", service.selectThree(vo));
		}
		
		return path + "WarehouseOrderHOXdmList"; 
		}
	//입고관리
	@RequestMapping(value = "/whxdm/whorder/InOrderWHXdmList")
	public String InOrderWHXdmList(Model model,@ModelAttribute("vo") OrderVo vo) {
		
		vo.setParamsPaging(service.selectOneCount(vo));
		
		if (vo.getTotalRows() > 0) {
			model.addAttribute("orderList", service.selectList(vo));
		}
		
		return "whxdm/whorder/InOrderWHXdmList"; 
		}
	//입고관리 폼
	@RequestMapping(value = "/whxdm/whorder/InOrderWHXdmForm")
	public String InOrderWHXdmForm(@ModelAttribute("vo") OrderVo vo,
			Model model, OrderDto orderDto) throws Exception {
		
		if (vo.getoSeq().equals("0") || vo.getoSeq().equals("")) {
			// insert mode
		} else {
			List<OrderDto> dtoList = service.selectOne(orderDto); // 주문한 상품이 여러개일 수 있으므로, List로 읽는다
			OrderDto dto = new OrderDto();
			dto = dtoList.get(0);
			model.addAttribute("orderItem", dto);
			model.addAttribute("orderList", dtoList);
		}
		return "whxdm/whorder/InOrderWHXdmForm";
	}
	//입고업데이트
	@ResponseBody
	@RequestMapping(value = "/whxdm/whorder/InOrderWHXdmUpdt")
	public Map<String, Object> InOrderWHXdmUpdt(OrderDto orderDto, 
			@RequestParam(value="opSeqList") List<String> opSeqList,
			@RequestParam(value="opStateCdList") List<String> opStateCdList) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		if (orderDto == null || opSeqList == null || opStateCdList == null) {
			returnMap.put("rt", "fail");
		} else {
			int successCnt = service.update(orderDto);
			
			if (successCnt > 0) {
				if (opSeqList.size() > 0 && opStateCdList.size() > 0) {
					int opSuccessCnt = 0;
					
					for (int i = 0; i < opSeqList.size(); i++) {
						OrderDto dto = new OrderDto();
						
						dto.setOpSeq(opSeqList.get(i));
						dto.setOpStateCd(opStateCdList.get(i));
						
						opSuccessCnt = opSuccessCnt + service.updateOPList(dto);
					}
					
					if (opSuccessCnt > 0) {
						returnMap.put("rt", "success");	
					} else {
						returnMap.put("rt", "fail");
					}
				} else {
					returnMap.put("rt", "success");	// 수정할 데이터 없으므로 바로 성공
				}
			} else {
				returnMap.put("rt", "fail");
			}
		}

		return returnMap;
	}
	//출고
	@RequestMapping(value = "/whxdm/whorder/OutOrderWHXdmList")
	public String OutOrderWHXdmList(Model model,@ModelAttribute("vo") OrderVo vo) {
		
		vo.setParamsPaging(service.selectOneCount(vo));
		
		if (vo.getTotalRows() > 0) {
			model.addAttribute("orderList", service.selectList(vo));
		}
		
		return "whxdm/whorder/OutOrderWHXdmList"; 
		}
	//출고 폼
	@RequestMapping(value = "/whxdm/whorder/OutOrderWHXdmForm")
	public String OutOrderWHXdmForm(@ModelAttribute("vo") OrderVo vo,
			Model model, OrderDto orderDto) throws Exception {
		
		if (vo.getoSeq().equals("0") || vo.getoSeq().equals("")) {
			// insert mode
		} else {
			List<OrderDto> dtoList = service.selectOne(orderDto); // 주문한 상품이 여러개일 수 있으므로, List로 읽는다
			OrderDto dto = new OrderDto();
			dto = dtoList.get(0);
			model.addAttribute("orderItem", dto);
			model.addAttribute("orderList", dtoList);
		}
		return "whxdm/whorder/OutOrderWHXdmForm";
	}
	//출고 업데이트
	@ResponseBody
	@RequestMapping(value = "/whxdm/whorder/OutOrderWHXdmUpdt")
	public Map<String, Object> OutOrderWHXdmUpdt(OrderDto orderDto, 
			@RequestParam(value="opSeqList") List<String> opSeqList,
			@RequestParam(value="opStateCdList") List<String> opStateCdList) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		if (orderDto == null || opSeqList == null || opStateCdList == null) {
			returnMap.put("rt", "fail");
		} else {
			int successCnt = service.update(orderDto);
			
			if (successCnt > 0) {
				if (opSeqList.size() > 0 && opStateCdList.size() > 0) {
					int opSuccessCnt = 0;
					
					for (int i = 0; i < opSeqList.size(); i++) {
						OrderDto dto = new OrderDto();
						
						dto.setOpSeq(opSeqList.get(i));
						dto.setOpStateCd(opStateCdList.get(i));
						
						opSuccessCnt = opSuccessCnt + service.updateOPList(dto);
					}
					
					if (opSuccessCnt > 0) {
						returnMap.put("rt", "success");	
					} else {
						returnMap.put("rt", "fail");
					}
				} else {
					returnMap.put("rt", "success");	// 수정할 데이터 없으므로 바로 성공
				}
			} else {
				returnMap.put("rt", "fail");
			}
		}

		return returnMap;
	}
	
	@RequestMapping(value = "/whxdm/whorder/DeliveryOrderWHXdmList")
	public String DeliveryOrderWHXdmList(Model model,@ModelAttribute("vo") OrderVo vo) {
		
		vo.setParamsPaging(service.selectOneCount(vo));
		
		if (vo.getTotalRows() > 0) {
			model.addAttribute("orderList", service.selectList(vo));
		}
		
		return "whxdm/whorder/DeliveryOrderWHXdmList"; 
		}
	
	@RequestMapping(value = "/hoxdm/order/DeliOrderHOXdmList")
	public String DeliOrderHOXdmList(Model model,@ModelAttribute("vo") OrderVo vo) {
		
		vo.setParamsPaging(service.selectOneCount(vo));
		
		if (vo.getTotalRows() > 0) {
			model.addAttribute("orderList", service.selectList(vo));
		}
		
		return "hoxdm/order/DeliOrderHOXdmList"; 
		}
}
