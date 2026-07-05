import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Product } from '../../model/product.model';
import { ProductsService } from '../../services/products.service';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './product-list.component.html'
})
export class ProductListComponent implements OnInit {
  private productsService = inject(ProductsService);

  products: Product[] = [];
  keyword = '';
  loading = false;
  errorMessage = '';

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.loading = true;
    this.errorMessage = '';
    this.productsService.getAll().subscribe({
      next: (data) => { this.products = data; this.loading = false; },
      error: (err) => { this.errorMessage = 'Unable to load products. Is the backend running on :8083?'; this.loading = false; console.error(err); }
    });
  }

  onSearch(): void {
    this.loading = true;
    this.productsService.search(this.keyword).subscribe({
      next: (data) => { this.products = data; this.loading = false; },
      error: (err) => { this.errorMessage = 'Search failed.'; this.loading = false; console.error(err); }
    });
  }

  onToggleSelected(product: Product): void {
    if (product.id == null) return;
    this.productsService.toggleSelected(product.id).subscribe({
      next: (updated) => { product.selected = updated.selected; },
      error: (err) => console.error(err)
    });
  }

  onDelete(product: Product): void {
    if (product.id == null) return;
    if (!confirm(`Delete product "${product.name}" ?`)) return;
    this.productsService.delete(product.id).subscribe({
      next: () => { this.products = this.products.filter(p => p.id !== product.id); },
      error: (err) => console.error(err)
    });
  }

  get totalStockValue(): number {
    return this.products.reduce((sum, p) => sum + p.price * p.quantity, 0);
  }
}
