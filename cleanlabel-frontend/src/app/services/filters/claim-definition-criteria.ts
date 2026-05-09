import { PagedCriteria } from './criteria';
import { HttpParams } from '@angular/common/http';

export class ClaimDefinitionCriteria extends PagedCriteria {
  search?: string;
  misleading?: boolean;
  type?: string;

  constructor(offset = 0, limit = 10) {
    super(offset, limit);
    this.filter = {};
  }

  public override toParams(): HttpParams {
    let options = super.toParams();
    if (this.search)           options = options.append('search', this.search);
    if (this.misleading != null) options = options.append('misleading', this.misleading.toString());
    if (this.type)             options = options.append('type', this.type);
    return options;
  }
}
