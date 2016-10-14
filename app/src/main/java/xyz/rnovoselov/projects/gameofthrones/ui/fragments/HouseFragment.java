package xyz.rnovoselov.projects.gameofthrones.ui.fragments;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import xyz.rnovoselov.projects.gameofthrones.R;
import xyz.rnovoselov.projects.gameofthrones.data.storage.models.Person;
import xyz.rnovoselov.projects.gameofthrones.utils.GotAvatarProcessor;

/**
 * Created by novoselov on 13.10.2016.
 */

public class HouseFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private PersonalAdapter mPersonalAdapter;

    public HouseFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = ((RecyclerView) inflater.inflate(R.layout.fragment_house, container, false));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<Person> persons = new ArrayList<>();
        persons.add(new Person(1l, 2l, 3l, "Roman Novoselov", true, "", 4l, 5l));
        mPersonalAdapter = new PersonalAdapter(persons);
        mRecyclerView.setAdapter(mPersonalAdapter);
        return mRecyclerView;
    }

    private class PersonalHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Person mPerson;
        private TextView mNameTextView;
        private TextView mTitleTextView;
        private ImageView mAvatarImageView;

        public PersonalHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mNameTextView = ((TextView) itemView.findViewById(R.id.iv_person_name));
            mTitleTextView = ((TextView) itemView.findViewById(R.id.iv_person_title));
            mAvatarImageView = ((ImageView) itemView.findViewById(R.id.iv_person_avatar));
        }

        public void bindPerson(Person person) {
            mPerson = person;
            mNameTextView.setText(person.getName());
            mTitleTextView.setText(person.getName());
            int px = getActivity().getResources().getDimensionPixelSize(R.dimen.size_rv_avatar);
            GotAvatarProcessor avatarProcessor = new GotAvatarProcessor(getActivity(), px, px);

            Bitmap bitmap = avatarProcessor
                    .setTextColor(Color.WHITE)
                    .setColorsArray(getActivity(), R.array.letter_colors)
                    .setTextFontSize(getActivity(), R.dimen.font_increased_16)
                    .getLetterTile(person.getName())
                    .transformToCircle()
                    .process();
            mAvatarImageView.setImageBitmap(bitmap);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), mPerson.getName(), Toast.LENGTH_LONG).show();
        }
    }

    private class PersonalAdapter extends RecyclerView.Adapter<PersonalHolder> {

        private List<Person> mPersons;

        public PersonalAdapter(List<Person> persons) {
            mPersons = persons;
        }

        @Override
        public PersonalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.person_item_rv, parent, false);
            return new PersonalHolder(view);
        }

        @Override
        public void onBindViewHolder(PersonalHolder holder, int position) {
            holder.bindPerson(mPersons.get(position));
        }

        @Override
        public int getItemCount() {
            return mPersons.size();
        }
    }
}
