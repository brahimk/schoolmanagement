import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ClasseService } from 'app/entities/classe/classe.service';
import { IClasse, Classe } from 'app/shared/model/classe.model';
import { Salle } from 'app/shared/model/enumerations/salle.model';
import { Creneau } from 'app/shared/model/enumerations/creneau.model';

describe('Service Tests', () => {
  describe('Classe Service', () => {
    let injector: TestBed;
    let service: ClasseService;
    let httpMock: HttpTestingController;
    let elemDefault: IClasse;
    let expectedResult: IClasse | IClasse[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ClasseService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Classe(0, 'AAAAAAA', 'AAAAAAA', Salle.SALLE_1, Creneau.MECREDI_MATIN, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Classe', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Classe()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Classe', () => {
        const returnedFromService = Object.assign(
          {
            nom: 'BBBBBB',
            niveau: 'BBBBBB',
            salle: 'BBBBBB',
            creneau: 'BBBBBB',
            comment: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Classe', () => {
        const returnedFromService = Object.assign(
          {
            nom: 'BBBBBB',
            niveau: 'BBBBBB',
            salle: 'BBBBBB',
            creneau: 'BBBBBB',
            comment: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Classe', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
