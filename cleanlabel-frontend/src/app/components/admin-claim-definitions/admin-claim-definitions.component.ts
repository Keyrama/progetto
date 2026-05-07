import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClaimDefinitionDTO, ClaimType, ValidationStrategy, MisleadingReason } from '../../models/product.model';
import { ProductService } from '../../services/product.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-admin-claim-definitions',
  templateUrl: './admin-claim-definitions.component.html',
  styleUrls: ['./admin-claim-definitions.component.scss'],
})
export class AdminClaimDefinitionsComponent implements OnInit {
  definitions: ClaimDefinitionDTO[] = [];
  loading = true;
  saving = false;

  showForm = false;
  editing: ClaimDefinitionDTO | null = null;
  form!: FormGroup;

  definitionToDelete: ClaimDefinitionDTO | null = null;

  filterText = '';
  filterType: string = '';
  filterMisleading: string = '';

  toastMsg = '';
  toastError = false;

  readonly claimTypes: ClaimType[] = ['NUTRITIONAL', 'HEALTH', 'MARKETING'];
  readonly validationStrategies: ValidationStrategy[] = [
    'NONE', 'NO_ARTIFICIAL_INGREDIENTS', 'SUGAR_BELOW_THRESHOLD',
    'FAT_REDUCED', 'HIGH_FIBER', 'NO_HIGH_RISK_INGREDIENTS'
  ];
  readonly misleadingReasons: MisleadingReason[] = [
    'NONE', 'NO_LEGAL_DEFINITION', 'VAGUE_CRITERIA', 'REGULATORY_BREACH'
  ];

  // CORPORATE can delete, SPECIALIST can only create/edit
  get canDelete(): boolean { return this.auth.hasRole('CORPORATE'); }

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    public auth: AuthService
  ) {}

  ngOnInit() { this.load(); }

  load() {
    this.loading = true;
    this.productService.getClaimDefinitions().subscribe(defs => {
      this.definitions = defs;
      this.loading = false;
    });
  }

  get filtered(): ClaimDefinitionDTO[] {
    return this.definitions.filter(d => {
      const matchText = !this.filterText ||
        d.term.toLowerCase().includes(this.filterText.toLowerCase());
      const matchType = !this.filterType || d.claimType === this.filterType;
      const matchMisleading = this.filterMisleading === ''
        || (this.filterMisleading === 'true' && d.misleading)
        || (this.filterMisleading === 'false' && !d.misleading);
      return matchText && matchType && matchMisleading;
    });
  }

  openCreate() {
    this.editing = null;
    this.form = this.buildForm(null);
    this.showForm = true;
  }

  openEdit(def: ClaimDefinitionDTO) {
    this.editing = def;
    this.form = this.buildForm(def);
    this.showForm = true;
  }

  closeForm() {
    this.showForm = false;
    this.editing = null;
  }

  private buildForm(def: ClaimDefinitionDTO | null): FormGroup {
    return this.fb.group({
      term:                [def?.term                ?? '', Validators.required],
      claimType:           [def?.claimType           ?? 'NUTRITIONAL', Validators.required],
      regulated:           [def?.regulated           ?? false],
      misleading:          [def?.misleading          ?? false],
      misleadingReason:    [def?.misleadingReason    ?? 'NONE'],
      explanation:         [def?.explanation         ?? '', Validators.required],
      regulatoryReference: [def?.regulatoryReference ?? ''],
      validationStrategy:  [def?.validationStrategy  ?? 'NONE', Validators.required],
      validationThreshold: [def?.validationThreshold ?? null],
    });
  }

  onSubmit() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving = true;

    const payload: ClaimDefinitionDTO = {
      ...this.form.value,
      validationThreshold: this.form.value.validationThreshold || null,
      regulatoryReference: this.form.value.regulatoryReference || null,
    };

    const op = this.editing?.id
      ? this.productService.updateClaimDefinition(this.editing.id, payload)
      : this.productService.createClaimDefinition(payload);

    op.subscribe({
      next: () => {
        this.saving = false;
        this.closeForm();
        this.load();
        this.showToast(this.editing ? 'Claim aggiornato.' : 'Claim creato.');
      },
      error: () => {
        this.saving = false;
        this.showToast('Errore durante il salvataggio.', true);
      }
    });
  }

  confirmDelete(def: ClaimDefinitionDTO) { this.definitionToDelete = def; }
  cancelDelete() { this.definitionToDelete = null; }

  doDelete() {
    if (!this.definitionToDelete?.id) return;
    this.productService.deleteClaimDefinition(this.definitionToDelete.id).subscribe({
      next: () => {
        this.definitionToDelete = null;
        this.load();
        this.showToast('Claim eliminato.');
      },
      error: () => {
        this.definitionToDelete = null;
        this.showToast('Errore durante l\'eliminazione.', true);
      }
    });
  }

  claimTypeBadge(type: ClaimType): string {
    if (type === 'NUTRITIONAL') return 'bg-success';
    if (type === 'HEALTH')      return 'bg-primary';
    return 'bg-secondary';
  }

  showToast(msg: string, error = false) {
    this.toastMsg = msg;
    this.toastError = error;
    setTimeout(() => this.toastMsg = '', 3000);
  }

  get f() { return this.form?.controls ?? {}; }
}
