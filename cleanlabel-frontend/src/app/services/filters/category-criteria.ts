import { PagedCriteria } from './criteria';

export class CategoryCriteria extends PagedCriteria {
  constructor(offset = 0, limit = 10) {
    super(offset, limit);
    this.filter = {};
  }
}
