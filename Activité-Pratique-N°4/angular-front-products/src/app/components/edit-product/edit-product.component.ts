import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ProductsService } from '../../services/products.service';

@Component({
  selector: 'app-edit-product',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './edit-product.component.html'
})
export class EditProductComponent implements OnInit {
  private fb = inject(FormBuilder);
  private productsService = inject(ProductsService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  submitted = false;
  errorMessage = '';
  productId!: number;

  productForm = this.fb.group({
    id: [{ value: 0, disabled: true }],
    name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(60)]],
    price: [0, [Validators.required, Validators.min(0)]],
    quantity: [0, [Validators.required, Validators.min(0)]],
    selected: [false]
  });

  get f() { return this.productForm.controls; }

  ngOnInit(): void {
    this.productId = Number(this.route.snapshot.paramMap.get('id'));
    this.productsService.getById(this.productId).subscribe({
      next: (p) => this.productForm.patchValue(p),
      error: (err) => { this.errorMessage = 'Product not found.'; console.error(err); }
    });
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.productForm.invalid) return;

    this.productsService.update(this.productId, this.productForm.getRawValue() as any).subscribe({
      next: () => this.router.navigateByUrl('/products'),
      error: (err) => { this.errorMessage = 'Failed to update product.'; console.error(err); }
    });
  }
}
