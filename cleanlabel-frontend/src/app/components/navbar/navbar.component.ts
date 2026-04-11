import { Component } from '@angular/core';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
})
export class NavbarComponent {
  get useMock() { return this.productService.useMock; }
  constructor(private productService: ProductService) {}
}
