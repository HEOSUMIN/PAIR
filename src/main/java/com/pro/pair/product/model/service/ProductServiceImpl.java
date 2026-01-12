package com.pro.pair.product.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pro.pair.product.model.dao.ProductMapper;
import com.pro.pair.product.model.dto.BrandDTO;
import com.pro.pair.product.model.dto.CategoryDTO;
import com.pro.pair.product.model.dto.ProductDTO;
import com.pro.pair.upload.model.dto.AttachmentDTO;

@Service("productService")
public class ProductServiceImpl implements ProductService{
	private ProductMapper productMapper;
	
	@Autowired
	public ProductServiceImpl(ProductMapper productMapper) {
		this.productMapper = productMapper;
	}
	
	@Override
	public List<BrandDTO> getBrandList() {
		// TODO Auto-generated method stub
		return productMapper.getBrandList();
	}

	@Override
	public List<CategoryDTO> getCategoryList() {
		return productMapper.getCategoryList();
	}

	@Override
	public List<CategoryDTO> getSubCategoryList(int categoryNo) {
		return productMapper.getSubCategoryList(categoryNo);
	}

	@Override
	public int checkCurrProdNo() {
		return productMapper.checkCurrProdNo();
	}

	@Override
	public int attachProdThumbnail(AttachmentDTO attachment) {
		return productMapper.attachProdThumbnail(attachment);
	}

	@Override
	public int addProduct(ProductDTO product) {
		int result = productMapper.addProduct(product);
		
		return result;
	}

	

}
