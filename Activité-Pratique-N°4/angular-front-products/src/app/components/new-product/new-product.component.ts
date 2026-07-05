import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ProductsService } from '../../services/products.service';

@Component({
  selector: 'app-new-product',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './new-product.component.html'
})
export class NewProductComponent {
  private fb = inject(FormBuilder);
  private productsService = inject(ProductsService);
  private router = inject(Router);

  submitted = false;
  errorMessage = '';

  productForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(60)]],
    price: [0, [Validators.required, Validators.min(0)]],
    quantity: [0, [Validators.required, Validators.min(0)]],
    selected: [false]
  });

  get f() { return this.productForm.controls; }

  onSubmit(): void {
    this.submitted = true;
    if (this.productForm.invalid) return;

    this.productsService.save(this.productForm.value as any).subscribe({
      next: () => this.router.navigateByUrl('/products'),
      error: (err) => { this.errorMessage = 'Failed to save product.'; console.error(err); }
    });
  }
}
