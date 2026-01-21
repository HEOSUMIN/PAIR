package com.pro.pair.product.model.service;

import java.util.List;
import java.util.Map;

import com.pro.pair.paging.model.dto.Criteria;
import com.pro.pair.paging.model.dto.ItemCriteria;
import com.pro.pair.product.model.dto.BrandDTO;
import com.pro.pair.product.model.dto.CategoryDTO;
import com.pro.pair.product.model.dto.OptionDTO;
import com.pro.pair.product.model.dto.ProductDTO;
import com.pro.pair.upload.model.dto.AttachmentDTO;

public interface ProductService {
	
	List<BrandDTO> getBrandList();		//카테고리리스트
	
	List<CategoryDTO> getCategoryList();		//카테고리리스트 
		
	List<CategoryDTO> getSubCategoryList(int categoryNo);		//서브카테고리리스트 
	
	int checkCurrProdNo();		//현재상품번호조회
	
	int attachProdThumbnail(AttachmentDTO attachment);		//상품 썸네일 등록
	
	int addProduct(ProductDTO product);				//상품등록
	
	int getTotalNumber(Criteria criteria);		//전체상품개수
	
	int getOnSaleNumber(Criteria criteria);		//판매중인상품개수
	
	//상품목록조회 
	List<ProductDTO> getProductList(Criteria criteria);
		
	List<ProductDTO> getOnSaleOnly(Criteria criteria);
	
	//상품상세조회
	ProductDTO getProductDetails(int prodNo);
	
	AttachmentDTO getMainThumbnailByProdNo(int prodNo);
	
	AttachmentDTO getSubThumbnailByProdNo(int prodNo);
	
	int addOption(int productNo, Map<String, Object> optionData);
	
	List<ProductDTO> getProductListByCategorySection(ItemCriteria itemCriteria);
	
	List<OptionDTO> getOptionListByProdNo(int prodNo);
}
