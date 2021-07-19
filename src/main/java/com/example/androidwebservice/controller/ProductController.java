package com.example.androidwebservice.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.androidwebservice.model.Product;
import com.example.androidwebservice.repository.IProductRepository;

import lombok.AllArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/productos")
@AllArgsConstructor
public class ProductController {

	private final IProductRepository iProductRepository;

	/*
	 * Inserta un producto
	 */
	@PostMapping
	public ResponseEntity<Product> insert(Product product) {
		String stringBase64 = product.getImageName();
		String imageName = getImageNameAfterSaveImage(stringBase64);
		product.setImageName(imageName);

		return new ResponseEntity<Product>(iProductRepository.save(product), HttpStatus.OK);
	}

	/*
	 * Retorna lista de productas
	 */
	@GetMapping
	public ResponseEntity<List<Product>> getProducts() {
		return new ResponseEntity<List<Product>>(iProductRepository.findAll(), HttpStatus.OK);
	}

	/*
	 * Retorna un producto por su id
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable("id") Long id) {
		return new ResponseEntity<Product>(iProductRepository.findById(id).orElse(null), HttpStatus.OK);
	}

	/*
	 * Obtener nombre de la im
	 */
	public String getImageNameAfterSaveImage(String stringBase64) {

		String nameFile = "";

		if (!stringBase64.isEmpty()) {
			String staticFolderPath = getStaticFolderPath();
			nameFile = generateImageName();
			saveImageInStaticFolder(stringBase64, staticFolderPath, nameFile);
		}
		return nameFile;
	}

	private String getStaticFolderPath() {
		Path directorioRescuros = Paths.get("src//main//resources//static");
		String rootPath = directorioRescuros.toFile().getAbsolutePath();
		return rootPath;
	}

	private String generateImageName() {
		return UUID.randomUUID().toString() + ".jpg";
	}

	private void saveImageInStaticFolder(String stringBase64, String staticFolderPath, String imageName) {
		try {
			byte[] decodedBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(stringBase64);
			Path rutaCompleta = Paths.get(staticFolderPath + "//" + imageName);
			Files.write(rutaCompleta, decodedBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
