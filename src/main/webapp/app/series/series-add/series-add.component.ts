import { Component, OnInit, OnDestroy, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { SeriesService } from './../series.service';
import { Series } from './../series.model';
import swal from 'sweetalert2';

@Component({
    selector: 'jhi-series-add',
    templateUrl: './series-add.component.html'
})
export class SeriesAddComponent implements OnInit {
    isSaving: boolean;
    serie: Series;
    serieName: any;
    serieClass: any;
    constructor(private router: Router, private seriesService: SeriesService) {}

    ngOnInit() {
        this.serie = {};
        console.log('serie.seriesName', this.serie.seriesName);
    }

    fun(data) {
        this.serieName = data;
        this.serie.seriesName = this.serieName;
        this.fun3();
    }

    fun2(data1) {
        console.log('classs data', data1);
        this.serieClass = data1;
        this.fun3();
    }

    fun3() {
        if (this.serieClass !== undefined && this.serieName !== undefined) {
            this.serie.seriesCode = this.serieClass + ' ' + '(' + this.serie.seriesName + ')';
        } else if (this.serieClass === undefined) {
            this.serie.seriesCode = '(' + this.serie.seriesName + ')';
        } else if (this.serieName === undefined) {
            this.serie.seriesCode = this.serieClass;
        }
        console.log('this.serie.seriesCode', this.serie.seriesCode);
    }

    onSubmit() {
        this.seriesService.add(this.serie).subscribe(data => {
            if (data) {
                this.router.navigate(['/series']);
                swal('Series added Successfully');
            } else {
            }
        });
    }

    cancel() {
        this.router.navigate(['/series']);
    }
}
