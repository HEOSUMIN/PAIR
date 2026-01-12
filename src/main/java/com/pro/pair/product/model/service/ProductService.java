package com.pro.pair.product.model.service;

import java.util.List;

import com.pro.pair.product.model.dto.BrandDTO;
import com.pro.pair.product.model.dto.CategoryDTO;
import com.pro.pair.product.model.dto.ProductDTO;
import com.pro.pair.upload.model.dto.AttachmentDTO;

public interface ProductService {
	
	List<BrandDTO> getBrandList();		//카테고리리스트
	
	List<CategoryDTO> getCategoryList();		//카테고리리스트 
		
	List<CategoryDTO> getSubCategoryList(int categoryNo);		//서브카테고리리스트 
	
	int checkCurrProdNo();		//현재상품번호조회
	
	int attachProdThumbnail(AttachmentDTO attachment);		//상품 썸네일 등록
	
	int addProduct(ProductDTO product);
}
