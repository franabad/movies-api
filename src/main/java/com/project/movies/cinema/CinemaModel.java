package com.project.movies.cinema;

import com.project.movies.utils.JsonConverter;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "cinemas")
public class CinemaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int capacity;

    @Column(name = "`rows`")
    private int rows;
    @Column(name = "`columns`")
    private int columns;

    @Column(name = "layout")
    @Convert(converter = JsonConverter.class)
    private List<LayoutSection> layout;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public List<LayoutSection> getLayout() {
        return layout;
    }

    public void setLayout(List<LayoutSection> layout) {
        this.layout = layout;
    }

    public static class LayoutSection {
        private String type;
        private Integer rows;
        private Integer columns;

        // Campos adicionales para el tipo "expanded"
        private Integer start_row;
        private List<Aisle> aisle;

        public static class Aisle {
            private Integer start;
            private Integer end;

            public Integer getStart() {
                return start;
            }

            public void setStart(Integer start) {
                this.start = start;
            }

            public Integer getEnd() {
                return end;
            }

            public void setEnd(Integer end) {
                this.end = end;
            }
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getRows() {
            return rows;
        }

        public void setRows(Integer rows) {
            this.rows = rows;
        }

        public Integer getColumns() {
            return columns;
        }

        public void setColumns(Integer columns) {
            this.columns = columns;
        }

        public Integer getStart_row() {
            return start_row;
        }

        public void setStart_row(Integer start_row) {
            this.start_row = start_row;
        }

        public List<Aisle> getAisle() {
            return aisle;
        }

        public void setAisle(List<Aisle> aisle) {
            this.aisle = aisle;
        }
    }


}
