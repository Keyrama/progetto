import { HttpParams } from '@angular/common/http';

export abstract class Criteria {
  filter: any;

  public toParams(): HttpParams {
    let options: HttpParams = new HttpParams();

    if (this.filter) {
      Object.keys(this.filter).forEach((key: string) => {
        const field: any = this.filter[key];
        if (field == null || field === '') return;

        if (Array.isArray(field)) {
          options = options.appendAll({ [key]: field });
        } else {
          options = options.append(key, field);
        }
      });
    }

    return options;
  }
}

export abstract class PagedCriteria extends Criteria {
  offset: number;
  limit: number;

  protected constructor(offset: number, limit: number) {
    super();
    this.offset = offset;
    this.limit = limit;
  }

  public override toParams(): HttpParams {
    let options: HttpParams = super.toParams();

    if (this.offset) {
      options = options.append('offset', this.offset.toString());
    }
    if (this.limit) {
      options = options.append('limit', this.limit.toString());
    }

    return options;
  }
}
