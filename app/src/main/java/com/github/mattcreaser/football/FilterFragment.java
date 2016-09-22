package com.github.mattcreaser.football;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.EnumSet;

import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;


/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends Fragment {

  private Adapter mAdapter;

  public FilterFragment() {

  }

  private EnumSet<Position> mPositions = EnumSet.noneOf(Position.class);

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_filter, container, false);

    Bundle arguments = getArguments();
    ButterKnife.<EditText>findById(view, R.id.filter_name).setText(arguments.getString("name"));
    ButterKnife.<CheckBox>findById(view, R.id.filter_drafted).setChecked(arguments.getBoolean("drafted"));

    //noinspection unchecked
    EnumSet<Position> positions = (EnumSet<Position>) arguments.getSerializable("positions");
    if (positions != null) {
      ButterKnife.<CheckBox>findById(view, R.id.filter_position_qb).setChecked(!positions.contains(Position.QB));
      ButterKnife.<CheckBox>findById(view, R.id.filter_position_wr).setChecked(!positions.contains(Position.WR));
      ButterKnife.<CheckBox>findById(view, R.id.filter_position_rb).setChecked(!positions.contains(Position.RB));
      ButterKnife.<CheckBox>findById(view, R.id.filter_position_te).setChecked(!positions.contains(Position.TE));
      ButterKnife.<CheckBox>findById(view, R.id.filter_position_k).setChecked(!positions.contains(Position.K));
      ButterKnife.<CheckBox>findById(view, R.id.filter_position_def).setChecked(!positions.contains(Position.DEF));
      mPositions.addAll(positions);
    }

    ButterKnife.<CheckBox>findById(view, R.id.filter_favourites).setChecked(arguments.getBoolean("favourites"));

    ButterKnife.bind(this, view);

    return view;
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    mAdapter = (Adapter) context;
    mAdapter.fragmentAttached();
  }

  @Override public void onDetach() {
    super.onDetach();
    mAdapter.fragmentDetached();
    mAdapter = null;
  }

  @OnTextChanged(R.id.filter_name)
  public void onNameFilterChange(CharSequence name) {
    mAdapter.playerFilterName(name);
  }

  @OnCheckedChanged(R.id.filter_drafted)
  public void onDraftedFilterChange(boolean isChecked) {
    mAdapter.showDrafted(isChecked);
  }

  @OnCheckedChanged(R.id.filter_position_qb)
  public void onQbFilterChange(boolean checked) {
    if (!checked) {
      mPositions.add(Position.QB);
    } else {
      mPositions.remove(Position.QB);
    }
    mAdapter.setPositions(mPositions);
  }

  @OnCheckedChanged(R.id.filter_position_wr)
  public void onWrFilterChange(boolean checked) {
    if (!checked) {
      mPositions.add(Position.WR);
    } else {
      mPositions.remove(Position.WR);
    }
    mAdapter.setPositions(mPositions);
  }

  @OnCheckedChanged(R.id.filter_position_rb)
  public void onRbFilterChange(boolean checked) {
    if (!checked) {
      mPositions.add(Position.RB);
    } else {
      mPositions.remove(Position.RB);
    }
    mAdapter.setPositions(mPositions);
  }

  @OnCheckedChanged(R.id.filter_position_te)
  public void onTeFilterChange(boolean checked) {
    if (!checked) {
      mPositions.add(Position.TE);
    } else {
      mPositions.remove(Position.TE);
    }
    mAdapter.setPositions(mPositions);
  }

  @OnCheckedChanged(R.id.filter_position_k)
  public void onKFilterChange(boolean checked) {
    if (!checked) {
      mPositions.add(Position.K);
    } else {
      mPositions.remove(Position.K);
    }
    mAdapter.setPositions(mPositions);
  }

  @OnCheckedChanged(R.id.filter_position_def)
  public void onDefFilterChange(boolean checked) {
    if (!checked) {
      mPositions.add(Position.DEF);
    } else {
      mPositions.remove(Position.DEF);
    }
    mAdapter.setPositions(mPositions);
  }

  @OnCheckedChanged(R.id.filter_favourites)
  public void onChangeFavourites(boolean checked) {
    mAdapter.showFavourites(checked);
  }

  @OnClick(R.id.filter_done)
  public void onDoneClicked() {
    getActivity().getSupportFragmentManager().popBackStack();
  }

  public interface Adapter {
    void fragmentAttached();

    void fragmentDetached();

    void playerFilterName(CharSequence name);

    void showDrafted(boolean drafted);

    void showFavourites(boolean favouritesOnly);

    void setPositions(EnumSet<Position> positions);
  }
}
