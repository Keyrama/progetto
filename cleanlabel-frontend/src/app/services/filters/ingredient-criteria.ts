import { PagedCriteria } from './criteria';
import { HttpParams } from '@angular/common/http';

export class IngredientCriteria extends PagedCriteria {
  search?: string;
  artificial?: boolean;
  riskLevel?: string;

  constructor(offset = 0, limit = 10) {
    super(offset, limit);
    this.filter = {};
  }

  public override toParams(): HttpParams {
    let options = super.toParams();
    if (this.search)        options = options.append('search', this.search);
    if (this.artificial != null) options = options.append('artificial', this.artificial.toString());
    if (this.riskLevel)     options = options.append('riskLevel', this.riskLevel);
    return options;
  }
}
