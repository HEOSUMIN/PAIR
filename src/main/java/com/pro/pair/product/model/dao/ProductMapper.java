package com.pro.pair.product.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pro.pair.product.model.dto.BrandDTO;
import com.pro.pair.product.model.dto.CategoryDTO;
import com.pro.pair.product.model.dto.ProductDTO;
import com.pro.pair.upload.model.dto.AttachmentDTO;

@Mapper
public interface ProductMapper {
	List<BrandDTO> getBrandList();
	List<CategoryDTO> getCategoryList();
	List<CategoryDTO> getSubCategoryList(int categoryNo);		//서브카테고리리스트 
	int checkCurrProdNo();
	int attachProdThumbnail(AttachmentDTO attachment);	
	int addProduct(ProductDTO product);
	
}
