import { PagedCriteria } from './criteria';

export class IngredientCriteria extends PagedCriteria {
  constructor(offset = 0, limit = 10) {
    super(offset, limit);
    this.filter = {};
  }

  artificial?: boolean;
  riskLevel?: string;
}
