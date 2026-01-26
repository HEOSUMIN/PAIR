package com.pro.pair.product.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pro.pair.paging.model.dto.Criteria;
import com.pro.pair.paging.model.dto.ItemCriteria;
import com.pro.pair.product.model.dto.BrandDTO;
import com.pro.pair.product.model.dto.CategoryDTO;
import com.pro.pair.product.model.dto.OptionCombDTO;
import com.pro.pair.product.model.dto.OptionDTO;
import com.pro.pair.product.model.dto.OptionValueDTO;
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
	
	int getTotalNumber(Criteria criteria);
	
	int getOnSaleNumber(Criteria criteria);
	
	List<ProductDTO> getProductList(Criteria criteria);
	
	List<ProductDTO> getOnSaleOnly(Criteria criteria);
	
	ProductDTO getProductDetails(int prodNo);
	
	AttachmentDTO getMainThumbnailByProdNo(int prodNo);
	
	AttachmentDTO getSubThumbnailByProdNo(int prodNo);
	
	int addOption(List<OptionDTO> optionList);

	int insertOptionName(OptionDTO option);
	
	int insertOptionValue(OptionValueDTO optionValue);
	
	int insertOptionComb(OptionCombDTO optionComb);
	
	List<ProductDTO> getProductListByCategorySection(ItemCriteria itemCriteria);
	
	List<OptionDTO> getOptionListByProdNo(int prodNo);
	
	List<OptionCombDTO> getOptionDetailsListByProdNo(int prodNo);
	
	OptionCombDTO findOptionCombByName(int prodNo, String combName);
}
