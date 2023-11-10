package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Category;
import com.anakie.restApiBakery.entity.Product;
import com.anakie.restApiBakery.entity.ProductDTO;
import com.anakie.restApiBakery.exception.CategoryNotFoundException;
import com.anakie.restApiBakery.exception.ProductNotFoundException;
import com.anakie.restApiBakery.exception.RecipeNotFoundException;
import com.anakie.restApiBakery.repository.CategoryRepository;
import com.anakie.restApiBakery.repository.ProductRepository;
import com.anakie.restApiBakery.repository.RecipeRepository;
import org.hibernate.PropertyValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RecipeService recipeService;

    @Override
    public Product save(ProductDTO productDTO) throws RecipeNotFoundException, CategoryNotFoundException, PropertyValueException {

        Long recipeId=productDTO.getRecipeId();
        Long categoryId=productDTO.getCategoryId();

        if(!recipeService.existsById(recipeId)){
            throw new RecipeNotFoundException("Recipe id: "+recipeId+" is invalid, use existing recipe or create one");
        }
        if(!categoryService.existsById(categoryId)){
            throw new CategoryNotFoundException("Category id: "+ categoryId+" is invalid, use existing category or create one");
        }
        if(productDTO.getName()==null){
            throw new PropertyValueException("Product name can't be null","Product","name");
        }
        Product product=productDTO.toProduct(categoryService,recipeService);
        return productRepository.save(product);
    }
    @Override
    public Product findById(Long id) throws ProductNotFoundException {
        return productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Product "+id+" not found, use an existing id!"));
    }


    @Override
    public void deleteById(Long id) throws ProductNotFoundException {
        if(!productRepository.existsById(id)){
            throw new ProductNotFoundException("Can't delete product "+id+" not found, use an existing id!");
        }
        productRepository.deleteById(id);
    }

    @Override
    public Product update(ProductDTO productDTO) throws ProductNotFoundException, CategoryNotFoundException, RecipeNotFoundException {
        if(!productRepository.existsById(productDTO.getRecipeId())){
            throw new ProductNotFoundException("Can't update product "+productDTO.getRecipeId()+" not found, use an existing id!");
        }
        return productRepository.save(productDTO.toProduct(categoryService,recipeService));
    }
    @Override
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }
    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }
    @Override
    public double totalAmount(Long productId,int productQty) throws ProductNotFoundException {
        Product product=productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException("Product not found, refer to existing product"));
        return product.getPrice()*productQty;
    }

}
