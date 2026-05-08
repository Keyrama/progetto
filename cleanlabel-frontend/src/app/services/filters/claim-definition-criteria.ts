import { PagedCriteria } from './criteria';

export class ClaimDefinitionCriteria extends PagedCriteria {
  constructor(offset = 0, limit = 10) {
    super(offset, limit);
    this.filter = {};
  }

  misleading?: boolean;
  type?: string;
}
