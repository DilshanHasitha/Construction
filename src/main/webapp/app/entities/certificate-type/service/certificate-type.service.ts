import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICertificateType, NewCertificateType } from '../certificate-type.model';

export type PartialUpdateCertificateType = Partial<ICertificateType> & Pick<ICertificateType, 'id'>;

export type EntityResponseType = HttpResponse<ICertificateType>;
export type EntityArrayResponseType = HttpResponse<ICertificateType[]>;

@Injectable({ providedIn: 'root' })
export class CertificateTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/certificate-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(certificateType: NewCertificateType): Observable<EntityResponseType> {
    return this.http.post<ICertificateType>(this.resourceUrl, certificateType, { observe: 'response' });
  }

  update(certificateType: ICertificateType): Observable<EntityResponseType> {
    return this.http.put<ICertificateType>(`${this.resourceUrl}/${this.getCertificateTypeIdentifier(certificateType)}`, certificateType, {
      observe: 'response',
    });
  }

  partialUpdate(certificateType: PartialUpdateCertificateType): Observable<EntityResponseType> {
    return this.http.patch<ICertificateType>(`${this.resourceUrl}/${this.getCertificateTypeIdentifier(certificateType)}`, certificateType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICertificateType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICertificateType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCertificateTypeIdentifier(certificateType: Pick<ICertificateType, 'id'>): number {
    return certificateType.id;
  }

  compareCertificateType(o1: Pick<ICertificateType, 'id'> | null, o2: Pick<ICertificateType, 'id'> | null): boolean {
    return o1 && o2 ? this.getCertificateTypeIdentifier(o1) === this.getCertificateTypeIdentifier(o2) : o1 === o2;
  }

  addCertificateTypeToCollectionIfMissing<Type extends Pick<ICertificateType, 'id'>>(
    certificateTypeCollection: Type[],
    ...certificateTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const certificateTypes: Type[] = certificateTypesToCheck.filter(isPresent);
    if (certificateTypes.length > 0) {
      const certificateTypeCollectionIdentifiers = certificateTypeCollection.map(
        certificateTypeItem => this.getCertificateTypeIdentifier(certificateTypeItem)!
      );
      const certificateTypesToAdd = certificateTypes.filter(certificateTypeItem => {
        const certificateTypeIdentifier = this.getCertificateTypeIdentifier(certificateTypeItem);
        if (certificateTypeCollectionIdentifiers.includes(certificateTypeIdentifier)) {
          return false;
        }
        certificateTypeCollectionIdentifiers.push(certificateTypeIdentifier);
        return true;
      });
      return [...certificateTypesToAdd, ...certificateTypeCollection];
    }
    return certificateTypeCollection;
  }
}
