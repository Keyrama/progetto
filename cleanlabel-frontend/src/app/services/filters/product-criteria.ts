import { PagedCriteria } from './criteria';
import { HttpParams } from '@angular/common/http';

export class ProductCriteria extends PagedCriteria {
  search?: string;
  category?: number;
  cleanLabel?: boolean;

  constructor(offset = 0, limit = 5) {
    super(offset, limit);
    this.filter = {};
  }

  public override toParams(): HttpParams {
    let options = super.toParams();
    if (this.search)              options = options.append('search', this.search);
    if (this.category != null)    options = options.append('category', this.category.toString());
    if (this.cleanLabel === true) options = options.append('cleanLabel', 'true');
    return options;
  }
}
