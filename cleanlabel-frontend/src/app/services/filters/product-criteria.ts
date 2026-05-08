import { PagedCriteria } from './criteria';

export class ProductCriteria extends PagedCriteria {
  constructor(offset = 0, limit = 5) {
    super(offset, limit);
    this.filter = {};
  }

  search?: string;
  category?: number;
  cleanLabel?: boolean;
}
