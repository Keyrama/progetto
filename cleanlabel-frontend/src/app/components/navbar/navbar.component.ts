import { Component, HostListener } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { UserRole } from '../../models/product.model';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent {
  readonly roles: UserRole[] = ['CONSUMER', 'SPECIALIST', 'CORPORATE'];
  dropdownOpen = false;

  constructor(public auth: AuthService) {}

  selectRole(role: UserRole) {
    this.auth.login(role).subscribe();
    this.dropdownOpen = false;
  }

  logout() {
    this.auth.logout();
    this.dropdownOpen = false;
  }

  toggleDropdown() {
    this.dropdownOpen = !this.dropdownOpen;
  }

  /** Close dropdown when clicking anywhere outside */
  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const target = event.target as HTMLElement;
    if (!target.closest('.role-dropdown')) {
      this.dropdownOpen = false;
    }
  }

  roleLabel(role: UserRole): string {
    const map: Record<UserRole, string> = {
      CONSUMER: 'Consumatore',
      SPECIALIST: 'Specialista',
      CORPORATE: 'Corporate',
    };
    return map[role];
  }

  roleBadgeClass(role: UserRole | null): string {
    if (role === 'CORPORATE') return 'bg-danger';
    if (role === 'SPECIALIST') return 'bg-primary';
    return 'bg-secondary';
  }
}
