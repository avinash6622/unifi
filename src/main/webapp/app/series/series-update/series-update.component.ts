import { Component, OnInit, OnDestroy, EventEmitter } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { SeriesService } from './../series.service';
import { Series } from './../series.model';
import swal from 'sweetalert2';

@Component({
    selector: 'jhi-series-update',
    templateUrl: './series-update.component.html'
})
export class SeriesUpdateComponent implements OnInit {
    isSaving: boolean;
    serie: Series;
    serieName: any;
    serieClass: any;
    id: number;

    constructor(private router: Router, private route: ActivatedRoute, private seriesService: SeriesService) {}

    ngOnInit() {
        this.serie = {};
        this.route.params.subscribe((params: any) => {
            this.id = params.id;
            this.loadSeries(this.id);
        });
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
        this.serie.seriesCode = this.serieClass + ' ' + '(' + this.serie.seriesName + ')';
        console.log('this.serie.seriesCode', this.serie.seriesCode);
    }

    loadSeries(id) {
        this.seriesService.find(id).subscribe(serie => {
            this.serie = serie;
            console.log(this.serie);
        });
    }

    onSubmit() {
        this.seriesService.update(this.serie).subscribe(data => {
            if (data) {
                this.router.navigate(['/series']);
                swal('Series Updated Successfully');
            } else {
            }
        });
    }

    cancel() {}
}
