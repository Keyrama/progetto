import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { UserDTO, UserRole } from '../models/product.model';
import { environment } from '../../environments/environment';

/**
 * Manages the mock authenticated user.
 *
 * Simulates login via GET /api/users/mock/{role}.
 * Stores the current user in a BehaviorSubject so all components
 * can react to role changes without page reload.
 *
 * The current role is also persisted to sessionStorage so it
 * survives Angular navigation but resets on tab close.
 */
@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly STORAGE_KEY = 'cleanlabel_mock_user';
  private api = environment.apiUrl;

  private currentUserSubject = new BehaviorSubject<UserDTO | null>(this.loadFromStorage());
  currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {}

  get currentUser(): UserDTO | null {
    return this.currentUserSubject.value;
  }

  get currentRole(): UserRole | null {
    return this.currentUser?.role ?? null;
  }

  /** Returns the X-Mock-User-Role header value, or empty object if not logged in */
  get roleHeader(): { [key: string]: string } {
    const role = this.currentRole;
    return role ? { 'X-Mock-User-Role': role } : {};
  }

  hasRole(...roles: UserRole[]): boolean {
    return this.currentRole != null && roles.includes(this.currentRole);
  }

  /** Fetches a mock user for the given role and sets it as current */
  login(role: UserRole): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.api}/users/mock/${role}`).pipe(
      tap(user => this.setUser(user)),
      catchError(() => {
        // Backend unreachable — build a transient local mock
        const localMock: UserDTO = {
          username: `mock_${role.toLowerCase()}`,
          email: `mock_${role.toLowerCase()}@cleanlabel.mock`,
          role,
        };
        this.setUser(localMock);
        return of(localMock);
      })
    );
  }

  logout(): void {
    this.currentUserSubject.next(null);
    sessionStorage.removeItem(this.STORAGE_KEY);
  }

  private setUser(user: UserDTO): void {
    this.currentUserSubject.next(user);
    sessionStorage.setItem(this.STORAGE_KEY, JSON.stringify(user));
  }

  private loadFromStorage(): UserDTO | null {
    try {
      const raw = sessionStorage.getItem(this.STORAGE_KEY);
      return raw ? JSON.parse(raw) : null;
    } catch {
      return null;
    }
  }
}
